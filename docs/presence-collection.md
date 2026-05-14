# PresenceCollection & PresenceSet

Time-indexed collections of `Presence` objects. Think of them as timelines where each moment maps to exactly one location—perfect for tracking "where was Agent Martinez throughout their investigation?"

---

## TL;DR

**PresenceCollection** = A read-only querying interface for temporal timelines. Maps `Moment` to `Location` with efficient lookups (floor, ceiling, lower, higher), windowed queries, and iteration. Use this when you need to query an agent's timeline without modifying it. Classic Command-Query Responsibility Segregation (CQRS) pattern.

**PresenceSet** = A mutable implementation of `PresenceCollection`. Adds `.put()` and `.remove()` methods for building timelines. Perfect for tracking movement histories where location updates arrive over time. Despite TesseractLib's preference for immutability, PresenceSet is deliberately mutable for performance—adding thousands of location updates shouldn't require copying the entire timeline.

**Immutable snapshots:** Call `.snapshot()` on any PresenceCollection to get an immutable copy. Perfect for passing timelines around without worrying about concurrent modification.

**Factory pattern:** `PresenceSet` → `PresenceSets.presences()`. Create an empty mutable set and populate it with `.put()` operations.

**Key insight:** Each moment can have only ONE location. If you put a new presence at an existing moment, the old location is overwritten. This enforces the physical constraint that agents can't be in two places at once (even time-traveling agents).

---

## Table of Contents

- [Foundation](#foundation)
- [PresenceCollection (Read-Only Interface)](#presencecollection-read-only-interface)
  - [Floor and Ceiling](#floor-and-ceiling)
  - [Lower and Higher](#lower-and-higher)
  - [Iteration](#iteration)
  - [Immutable Snapshots](#immutable-snapshots)
- [PresenceSet (Mutable Implementation)](#presenceset-mutable-implementation)
  - [Creating PresenceSets](#creating-presencesets)
  - [Adding Presences](#adding-presences)
  - [Removing Presences](#removing-presences)
- [Practical Example](#practical-example)
- [Next Steps](#next-steps)

---

## Foundation

> **Interface Hierarchy:** `PresenceCollection` is the read-only querying interface. `PresenceSet extends PresenceCollection` and adds mutation methods (`.put()`, `.remove()`). This separation follows the Command-Query Responsibility Segregation (CQRS) pattern—queries don't modify state, commands don't return data.
> 
> **Immutability vs Mutability:** PresenceSet is **intentionally mutable** for performance when building timelines. However, you can call `.snapshot()` on any PresenceCollection to get an immutable copy. This gives you the best of both worlds: efficient construction with mutable PresenceSet, safe sharing with immutable snapshots.
> 
> **Uniqueness Constraint:** Each `Moment` maps to exactly one `Location`. Adding a presence at an existing moment overwrites the previous location. This enforces the physical reality that an agent can't be in two places simultaneously.
> 
> **Sorted Structure:** PresenceSet is backed by `TreeMap<Moment, Presence>` for O(log n) lookups and efficient range queries. The sorted tree structure enables fast floor/ceiling operations and submap views for windowed queries.
> 
> **Thread Safety:** PresenceSet is **not thread-safe**. If multiple threads need access, either use external synchronization or call `.snapshot()` to create thread-safe immutable copies for each thread. Like most mutable collections, concurrent modification without synchronization leads to unpredictable results—and nobody wants to debug why an agent appears to be in two cities simultaneously.

> **Examples notation:** Throughout this document, we use helper methods like `presence(hours, lat, lon)` and `moment(hours)` as shorthand. These create temporal and spatial objects relative to today's UTC midnight. For complete implementation details, see [Helper Notation](helper-notation.md).

---

## PresenceCollection (Read-Only Interface)

The `PresenceCollection` interface provides read-only access to timelines. Use it when you need to query agent locations without modification. All query methods (floor, ceiling, lower, higher, presences, movements) are available through this interface.

**When to use PresenceCollection:**
- Passing timelines to methods that only need to read data
- Sharing timelines between threads (via `.snapshot()`)
- Analysis and reporting where modification isn't needed
- Enforcing immutability at the API level


### Floor and Ceiling

Find the closest presence at or before/after a given moment:

```java
var timeline = PresenceSets.immutable(
    presence(moment(9), location(47.4979, 19.0402)),   // Budapest
    presence(moment(12), location(51.5074, -0.1278)),  // London
    presence(moment(15), location(48.8566, 2.3522))    // Paris
);

// floor(moment): Latest presence at or before the given moment
var whereAt10 = timeline.floor(moment(10));
// Returns: presence(moment(9), Budapest)
// "Where was the agent at 10:00?" → "Last known location was Budapest at 9:00"

var whereAt12 = timeline.floor(moment(12));
// Returns: presence(moment(12), London)
// Exact match - agent was at London at 12:00

var whereAt8 = timeline.floor(moment(8));
// Returns: null
// No presence before 9:00 - agent's timeline hasn't started yet

// ceiling(moment): Earliest presence at or after the given moment
var nextAfter10 = timeline.ceiling(moment(10));
// Returns: presence(moment(12), London)
// "What's the next recorded location after 10:00?" → "London at 12:00"

var nextAt12 = timeline.ceiling(moment(12));
// Returns: presence(moment(12), London)
// Exact match

var nextAfter16 = timeline.ceiling(moment(16));
// Returns: null
// No presence after 15:00 - agent's timeline ends
```

### Lower and Higher

Find the closest presence strictly before/after a given moment (excludes exact matches):

```java
// lower(moment): Latest presence BEFORE the given moment (excludes exact match)
var beforeNoon = timeline.lower(moment(12));
// Returns: presence(moment(9), Budapest)
// "Where was the agent BEFORE noon?" → "Budapest at 9:00"
// Note: London at 12:00 is NOT returned (lower is STRICTLY before)

var before9 = timeline.lower(moment(9));
// Returns: null
// No presence strictly before 9:00

// higher(moment): Earliest presence AFTER the given moment (excludes exact match)
var afterNoon = timeline.higher(moment(12));
// Returns: presence(moment(15), Paris)
// "Where was the agent AFTER noon?" → "Paris at 15:00"
// Note: London at 12:00 is NOT returned (higher is STRICTLY after)

var after15 = timeline.higher(moment(15));
// Returns: null
// No presence strictly after 15:00
```

**Use cases for floor vs lower:**

```java
// floor: "Where was the agent at this moment (or last known location)?"
// Use for: Timeline reconstruction, "current location" queries
var currentLocation = timeline.floor(moment(10));  // Includes exact match

// lower: "Where was the agent BEFORE this moment (strictly)?"
// Use for: "Previous location" queries, change detection
var previousLocation = timeline.lower(moment(12));  // Excludes exact match
```

---

## Iteration

PresenceCollection provides fluent iterators for traversing timelines. The returned `FluentIterator<T>` supports stream-like operations without the overhead of creating intermediate collections.

### Basic Iteration

```java
var timeline = PresenceSets.immutable(
    presence(moment(8), location(47.4979, 19.0402)),    // Budapest
    presence(moment(12), location(51.5074, -0.1278)),   // London
    presence(moment(16), location(48.8566, 2.3522))     // Paris
);

// Iterate all presences
timeline.presences().forEach(p -> 
    System.out.println(p.getMoment() + " → " + p.getLocation())
);

// Convert to list
var allPresences = timeline.presences().list();

// Forward iteration within time window
var workDay = interval(9, 17);
var duringWork = timeline.presences(workDay.forward()).list();
// Includes boundary presences at 9:00 and 17:00

// Backward iteration (reverse chronological)
var reversed = timeline.presences(workDay.backward()).list();
```


### Querying Movements

Movements represent transitions between consecutive presences:

```java
// All movements
var allMoves = timeline.movements().list();

// Movements within window
var dayMoves = timeline.movements(interval(8, 20).forward()).list();

// Calculate total distance
var distance = timeline.movements()
    .map(m -> m.getOrigin().getLocation().distanceTo(m.getDestination().getLocation()))
    .stream()
    .reduce(Distances.zero(), Distance::plus);
```

### Boundary Presence Generation

When querying with `presences(DirectedInterval)` or `movements(DirectedInterval)`, boundary presences are automatically generated at the interval edges:

- **Lower bound**: Uses the last known location before the window (or `Location.anywhere()` if none exists)
- **Upper bound**: Uses the next known location after the window (or `Location.anywhere()` if none exists)

```java
var timeline = PresenceSets.presences()
    .put(moment(8), location(47.4979, 19.0402))    // Budapest
    .put(moment(12), location(51.5074, -0.1278))   // London
    .put(moment(20), location(50.0755, 14.4378));  // Prague

// Query [10:00..18:00) generates boundaries:
var presences = timeline.presences(interval(10, 18).forward()).list();
// [0] presence(10, Budapest) ← last known before window
// [1] presence(12, London)   ← actual presence
// [2] presence(18, Prague)   ← next known after window

// Query from beginning [0:00..15:00):
timeline.presences(interval(0, 15).forward()).forEach(System.out::println);
// Output:
// anywhere@00:00     ← no previous location
// 47.4979,19.0402@08:00
// 51.5074,-0.1278@12:00
// 50.0755,14.4378@15:00  ← next location (Prague at 20:00)
```

This ensures movements always have proper origin/destination pairs at query boundaries.

---

## Immutable Snapshots

Any PresenceCollection can be converted to an immutable snapshot using `.snapshot()`. This creates a thread-safe, immutable copy of the timeline.

```java
// Build a mutable timeline
var timeline = PresenceSets.presences()
    .put(moment(8), location(47.4979, 19.0402))    // Budapest
    .put(moment(12), location(51.5074, -0.1278))   // London
    .put(moment(16), location(48.8566, 2.3522));   // Paris

// Create immutable snapshot for analysis
var immutableTimeline = timeline.snapshot();

// The snapshot is independent - changes to original don't affect it
timeline.put(moment(20), location(50.0755, 14.4378));  // Add Prague
assertThat(timeline.size()).isEqualTo(4);         // Original has 4 presences
assertThat(immutableTimeline.size()).isEqualTo(3); // Snapshot still has 3

// Immutable snapshots are safe to share across threads
var analysisThread1 = new Thread(() -> {
    var count = immutableTimeline.presences(interval(0, 24).forward()).count();
    System.out.println("Thread 1: " + count + " presences");
});

var analysisThread2 = new Thread(() -> {
    var locations = immutableTimeline.presences()
        .map(Presence::getLocation)
        .distinct()
        .count();
    System.out.println("Thread 2: " + locations + " unique locations");
});

analysisThread1.start();
analysisThread2.start();
// Both threads can safely read the immutable snapshot concurrently
```

**When to use snapshots:**
- Sharing timelines across threads without synchronization
- Passing timelines to methods that shouldn't modify them
- Creating checkpoints during timeline construction
- Analysis and reporting where immutability guarantees are needed

---

## PresenceSet (Mutable Implementation)

PresenceSet extends PresenceCollection and adds mutation methods for building timelines. Use this when you need to construct a timeline by adding or removing presences.

### Creating PresenceSets

```java
import com.fieldcode.tesseract.presence.PresenceSets;

// Method 1: Empty set (most common)
var agentTimeline = PresenceSets.presences();
// Start with empty timeline, populate with .put() operations

// Method 2: From existing presences
var p1 = presence(moment(9), location(47.4979, 19.0402));
var p2 = presence(moment(12), location(51.5074, -0.1278));
var p3 = presence(moment(15), location(48.8566, 2.3522));

var timeline = PresenceSets.presences(p1, p2, p3);
// Creates set with three presences: Budapest at 9:00, London at 12:00, Paris at 15:00

// Method 3: From collection
var presenceList = List.of(p1, p2, p3);
var timelineFromList = PresenceSets.presences(presenceList);
// Useful when loading from database or external source
```

> **Design Decision: Why PresenceSet is Mutable**
> 
> The original PresenceSet design followed TesseractLib's immutability principles. This lasted exactly three days in production. The breaking point: tracking Agent Martinez's 47-stop European investigation required copying the entire timeline 47 times—each location update created a new collection. The system ground to a halt.
> 
> The lesson: elegant theory meets messy reality. For timeline construction with hundreds of location updates, mutability isn't a compromise—it's a requirement. Now, with `.snapshot()`, we get both: mutable PresenceSet for efficient construction, immutable snapshots for safe sharing.

---

## Adding Presences

PresenceSet provides multiple ways to add location data:

```java
var timeline = PresenceSets.presences();

// Method 1: Add Presence object
var checkIn = presence(moment(9), location(47.4979, 19.0402));
timeline.put(checkIn);
// Agent Martinez checks in at Budapest HQ at 9:00

// Method 2: Add Moment + Location
timeline.put(moment(12), location(51.5074, -0.1278));
// Agent arrives at London Branch at 12:00

// Method 3: Add OffsetDateTime + Location (convenience)
timeline.put(OffsetDateTime.now(), location(48.8566, 2.3522));
// Real-time location update using current timestamp

// Chaining: put() returns the set, so you can chain calls
timeline
    .put(moment(9), location(47.4979, 19.0402))
    .put(moment(12), location(51.5074, -0.1278))
    .put(moment(15), location(48.8566, 2.3522));
// Build timeline fluently
```

**Overwriting behavior:**

```java
var timeline = PresenceSets.presences();

// First: Agent at Budapest at 9:00
timeline.put(moment(9), location(47.4979, 19.0402));

// Later: Correction - Agent was actually at Vienna at 9:00
timeline.put(moment(9), location(48.2082, 16.3738));
// The Budapest location is OVERWRITTEN
// Timeline now contains only Vienna at 9:00

assertThat(timeline.size()).isEqualTo(1);  // Still only one presence
// Form TPO-3003 correction filed: "Agent location update - Vienna, not Budapest"
```

---

## Removing Presences

Remove entries from the timeline:

```java
var timeline = PresenceSets.presences()
    .put(moment(9), location(47.4979, 19.0402))    // Budapest
    .put(moment(12), location(51.5074, -0.1278))   // London
    .put(moment(15), location(48.8566, 2.3522));   // Paris

// Remove single moment
timeline.remove(moment(12));
// London entry at 12:00 removed
// Timeline now: Budapest at 9:00, Paris at 15:00

// Remove range (with inclusive/exclusive bounds)
timeline.remove(
    moment(10), true,   // Lower bound: 10:00 (inclusive)
    moment(14), false   // Upper bound: 14:00 (exclusive)
);
// Removes all presences where 10:00 <= moment < 14:00
// If timeline had entries at 10:00, 11:00, 13:00, they'd be removed
// 14:00 would NOT be removed (upper bound is exclusive)

// Use case: Redact part of an agent's timeline (form TPO-REDACT-99)
timeline.remove(
    moment(13), true,
    moment(15), true    // Inclusive: removes 15:00 entry too
);
// "Agent Martinez's activities between 13:00-15:00 are classified"
```

**Why remove ranges?**
- Data cleanup: Remove invalid entries
- Privacy: Redact sensitive timeline segments
- Testing: Clear portions of timeline for re-processing
- Corrections: Bulk-remove incorrect data before re-adding
- Actually, we don't: This method has never been called in production. The Temporal Permit Office prefers filing form TPO-MISTAKE-404 ("Request to Ignore Previous Submission") over actually deleting anything

---

## Practical Example

**Agent Timeline Tracking**

Build and query Agent Martinez's investigation timeline across multiple cities:

```java
// Build timeline from location updates
var timeline = PresenceSets.presences()
    .put(moment(8), location(47.4979, 19.0402))    // Budapest 8:00
    .put(moment(11), location(48.2082, 16.3738))   // Vienna 11:00
    .put(moment(18), location(48.2082, 16.3738))   // Still Vienna 18:00
    .put(moment(33), location(50.0755, 14.4378));  // Prague 9:00 next day

// Where was agent at 10:00?
var at10 = timeline.floor(moment(10));
assertThat(at10.getLocation()).isEqualTo(location(47.4979, 19.0402));  // Budapest

// What's next location after 12:00?
var after12 = timeline.ceiling(moment(12));
assertThat(after12.getMoment()).isEqualTo(moment(18));  // Vienna at 18:00

// How many unique locations?
var uniqueCount = timeline.presences()
    .map(Presence::getLocation)
    .distinct()
    .count();
assertThat(uniqueCount).isEqualTo(3);  // Budapest, Vienna, Prague

// Calculate investigation duration
var duration = java.time.Duration.between(
    timeline.presences().min((p1, p2) -> p1.getMoment().compareTo(p2.getMoment()))
        .orElseThrow().getMoment().getDateTime(),
    timeline.presences().max((p1, p2) -> p1.getMoment().compareTo(p2.getMoment()))
        .orElseThrow().getMoment().getDateTime()
);
assertThat(duration.toHours()).isEqualTo(25);  // 8:00 → 9:00 next day
```

**Demonstrates:** Building timelines, floor/ceiling queries, stream operations, duration calculations

---

## Next Steps

- Review [Spatial Primitives](spatial-primitives.md) to understand Presence, Movement, and Track
- Explore [Temporal Primitives](temporal-primitives.md) for Moment and Interval operations
- Check out [Interval Collections](interval-collection.md) for temporal set operations
- Return to [Documentation Index](fieldcode-tesseract.md) for complete TesseractLib overview

