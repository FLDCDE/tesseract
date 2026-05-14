# Interval Collections

Collections for storing and querying sets of time intervals. The workhorse of TesseractLib's scheduling capabilities.

## TL;DR

**IntervalSet** = mutable collection of non-overlapping time intervals with automatic merging. Add `[9:00..12:00)` + `[12:00..17:00)` → get `[9:00..17:00)`. Magic.

**Key operations:**

- `IntervalSets.disjoint()` - create empty set
- `set.include(interval)` - add/merge intervals
- `set.exclude(interval)` - punch holes
- `set.intervals()` - iterate all intervals
- `set.find(window, direction, duration)` - find first slot ≥ duration

**Boolean algebra on time:**

- `IntervalCollections.and(A, B)` - intersection (overlap)
- `IntervalCollections.or(A, B)` - union (combine)
- `IntervalCollections.not(A)` - complement (invert)

**Performance model:** Lazy views backed by TreeMap. O(log n) lookups, no intermediate storage, queries compute on-demand. Not thread-safe—wrap it yourself if needed.

**Use cases:** Availability tracking, meeting room bookings, uptime monitoring, any "when is this thing happening?" problem where intervals overlap/merge/split.

**Gotcha:** These are *views*—changes to source `IntervalSet` propagate immediately. Want a snapshot? Materialize with `IntervalSets.disjoint(view)`.

## Table of Contents

- [Overview](#overview)
- [IntervalSet](#intervalset)
  - [Creating Interval Sets](#creating-interval-sets)
  - [Auto-Merging Behavior](#auto-merging-behavior)
  - [Modifying Sets](#modifying-sets)
- [IntervalCollection](#intervalcollection)
- [Querying Operations](#querying-operations)
  - [Basic Iteration](#basic-iteration)
  - [Windowed Queries](#windowed-queries)
  - [Finding Intervals](#finding-intervals)
  - [Boundary Signals](#boundary-signals)
- [Collection Properties](#collection-properties)
- [Practical Examples](#practical-examples)
- [Common Patterns](#common-patterns)

---

## Overview

`IntervalCollection` is the interface that defines query operations on intervals. `IntervalSet` extends it with modification operations, storing **non-overlapping (disjoint)** intervals that automatically merge when they touch or overlap.

**Factory pattern:** Like other TesseractLib types, interval sets have a factory class: `IntervalSet` → `IntervalSets`.

**Key design choices:**

- **Disjoint storage**: Intervals never overlap—overlapping intervals merge automatically
- **Sorted order**: Intervals maintain temporal order internally
- **Immutable by default**: Most operations return new collections; `IntervalSet` is explicitly mutable
- **Lazy iteration**: Query results use iterators for memory efficiency

> **Examples notation:** Throughout this document, we use helper methods like `interval(9, 17)` as shorthand. These create temporal objects relative to today's beginning in UTC. For example, `interval(9, 17)` means [9:00..17:00) UTC today. For complete implementation details, see [Helper Notation](helper-notation.md).

## IntervalSet

A mutable collection that stores disjoint (non-overlapping) intervals. The killer feature: **automatic merging**.

> **Implementation note:** `IntervalSet` extends `IntervalCollection`, inheriting all query methods (`intervals()`, `find()`, `bounds()`, etc.) while adding modification operations (`include()`, `exclude()`). This means everything you can do with an `IntervalCollection` works on an `IntervalSet`, plus you get mutability.

> **Thread-safety note:** `IntervalSet` is **not thread-safe**. This is a deliberate design decision—we sacrificed synchronization on the altar of performance. If you need concurrent access, wrap it in your own synchronization or use immutable views (`IntervalCollections` operations return thread-safe views). For single-threaded scheduling operations (where TesseractLib excels), the performance gain is worth it.

### Creating Interval Sets

```java
import com.fieldcode.tesseract.interval.IntervalSets;

// Empty set
var emptySet = IntervalSets.disjoint();

// From intervals - overlapping ones merge automatically
var set = IntervalSets.disjoint(
    interval(5, 10),   // [5:00..10:00)
    interval(12, 18),  // [12:00..18:00)
    interval(8, 14)    // [8:00..14:00) - overlaps with first two!
);

// Result: Merged into [5:00..18:00)
set.intervals().forEach(System.out::println);
// Output: [5:00..18:00)
```

> **"Where's `IntervalSets.joint()`?"** Great question! If we have `disjoint()`, surely we should have `joint()` for overlapping intervals, right? Well... we didn't have time, and more importantly, we didn't have a requirement for it. Turns out, storing overlapping intervals without merging them is surprisingly niche. Plus, naming it `joint()` would've been too clever by half. We're already stretching it with the disjoint/joint pun. Sometimes you just have to pick your battles, and this one wasn't worth fighting. If you need overlapping intervals, consider using multiple sets or an `IntervalContainer`. Or, you know, submit a PR. We promise to review it... eventually.

**From a collection:**
```java
var intervals = List.of(interval(9, 17), interval(18, 22));
var set = IntervalSets.disjoint(intervals);
```

**From custom objects using mappers:**

Often you have domain objects with temporal fields and want to create an interval set from them. Your entities and DTOs won't magically transform into `Interval` objects (we tried staring). Instead, use mapper functions to teach TesseractLib how to extract "when does this start?" and "when does it end?" from your objects.

```java
// Define your domain object
record Event(String id, OffsetDateTime from, OffsetDateTime to) {}

// Create a collection of events
var events = List.of(
    new Event("meeting-1", at(9), at(10)),
    new Event("meeting-2", at(14), at(16)),
    new Event("meeting-3", at(15), at(17))  // Overlaps with meeting-2
);

// Create interval set using mappers
var set = IntervalSets.disjoint(events, Event::from, Event::to);

// Result: [9:00..10:00), [14:00..17:00) - meetings 2 and 3 merged
set.intervals().forEach(System.out::println);
```

This pattern is particularly useful when working with entities, DTOs, or any objects that have temporal boundaries but aren't themselves intervals.

### Auto-Merging Behavior

The magic happens automatically. Add overlapping or adjacent intervals, and they merge:

```java
var set = IntervalSets.disjoint();

set.include(interval(5, 10));    // [5:00..10:00)
set.include(interval(12, 18));   // Now: [5:00..10:00), [12:00..18:00)
set.include(interval(10, 12));   // Bridges the gap! Now: [5:00..18:00)

set.intervals().forEach(System.out::println);
// Output: [5:00..18:00)
```

**Why auto-merge?** Prevents representation ambiguity. `[9:00..12:00)` and `[12:00..17:00)` are stored as `[9:00..17:00)`. No gaps, no overlaps, no confusion.

**Touching intervals and the topology of time:**

Adjacent intervals merge automatically—a property that emerges naturally from half-open semantics. Consider tracking SLA compliance for a distributed system where uptime is measured continuously but maintenance windows must be excluded.

```java
var uptimeTracking = IntervalSets.disjoint();

// Primary datacenter operational
uptimeTracking.include(interval(0, 12));    // [00:00..12:00)

// Failover datacenter takes over
uptimeTracking.include(interval(12, 24));   // [12:00..24:00)

// At midnight (12:00), the primary's interval excludes the endpoint while 
// the failover's interval includes the startpoint. Mathematically, these are 
// adjacent but non-overlapping sets. The union operation collapses them into 
// a single continuous interval. No gaps, no double-counting, no special cases.

System.out.println("Service availability:");
uptimeTracking.intervals().forEach(System.out::println);
// Output: [00:00..24:00)

// Scheduled maintenance interrupts service
uptimeTracking.exclude(interval(12, 13));   // [12:00..13:00)
// Observe: exclude() performs set subtraction. The interval [00:00..24:00) 
// minus [12:00..13:00) decomposes into exactly [00:00..12:00) ∪ [13:00..24:00)

System.out.println("After scheduled maintenance:");
uptimeTracking.intervals().forEach(System.out::println);
// Output: [00:00..12:00), [13:00..24:00)

// Emergency patch extends availability window
uptimeTracking.include(interval(24, 26));   // [24:00..26:00) - next day
// This touches [13:00..24:00) at 24:00, triggering another merge

// Post-deployment monitoring excludes final hour
uptimeTracking.exclude(interval(25, 26));   // [25:00..26:00)

System.out.println("Final uptime intervals:");
uptimeTracking.intervals().forEach(System.out::println);
// Output: [00:00..12:00), [13:00..25:00)
// Three inclusions, two exclusions, and the disjoint invariant holds.
```

**Theoretical observations from imaginary mathematicians (possibly alien):**

The behavior here isn't arbitrary—it's a consequence of treating time as a linear continuum with discrete event boundaries. Half-open intervals form a proper partition of the timeline, where:

- **Adjacency implies mergability**: If `I₁.upper = I₂.lower`, then `I₁ ∪ I₂` is well-defined and contiguous. No floating-point epsilon nonsense, no "are these timestamps *really* equal?" debates. They either match or they don't.
- **Subtraction is exact**: `[a..b) \ [c..d)` produces at most two intervals with no epsilon-handling required. The math works out cleanly, which would make mathematicians happy if they ever left their offices long enough to schedule a meeting.
- **Composition is associative**: `(I₁ ∪ I₂) ∪ I₃ = I₁ ∪ (I₂ ∪ I₃)`, and the internal representation automatically normalizes. You can compose operations without worrying about order-dependent bugs. This is the kind of thing that should be boring but somehow isn't.

**Set Operations on Interval Collections:**

The real power emerges when you combine multiple interval collections using set operations. `IntervalCollections` provides three fundamental operations that follow boolean algebra axioms:

- **`and()`** - Set intersection (logical AND): intervals present in ALL collections
- **`or()`** - Set union (logical OR): intervals present in ANY collection
- **`not()`** - Set complement (logical NOT): inversion of time coverage

These are genuine boolean algebra operations on temporal sets. Let's solve the uptime tracking problem using these operators instead of manual include/exclude:

```java
import com.fieldcode.tesseract.collection.IntervalCollections;

// Define potential uptime windows for different datacenters
var primaryDatacenter = IntervalSets.disjoint(
    interval(0, 12),    // Primary operational [00:00..12:00)
    interval(24, 36)    // Primary operational next day [24:00..36:00)
);

var failoverDatacenter = IntervalSets.disjoint(
    interval(12, 24),   // Failover operational [12:00..24:00)
    interval(36, 48)    // Failover operational next day [36:00..48:00)
);

// Scheduled maintenance windows (system unavailable)
var maintenanceWindows = IntervalSets.disjoint(
    interval(12, 13),   // Maintenance [12:00..13:00)
    interval(38, 39)    // Maintenance next day [38:00..39:00)
);

// Calculate actual uptime using set operations
// Uptime = (Primary OR Failover) AND NOT Maintenance
// This is boolean algebra on interval sets

// Step 1: Combine datacenter availability (union)
var combinedDatacenters = IntervalCollections.or(primaryDatacenter, failoverDatacenter);
System.out.println("Combined datacenter availability:");
combinedDatacenters.intervals().forEach(System.out::println);
// Output: [00:00..24:00), [24:00..48:00) - continuous coverage from both

// Step 2: Subtract maintenance windows (complement)
var actualUptime = IntervalCollections.and(
    combinedDatacenters,
    IntervalCollections.not(maintenanceWindows)
);

System.out.println("Actual uptime (excluding maintenance):");
actualUptime.intervals().forEach(System.out::println);
// Output: [00:00..12:00), [13:00..24:00), [24:00..38:00), [39:00..48:00)
// Maintenance windows [12:00..13:00) and [38:00..39:00) are excluded

// More complex: Find overlap between primary and failover (should be empty)
var overlap = IntervalCollections.and(primaryDatacenter, failoverDatacenter);
if (overlap.isNever()) {
    System.out.println("✓ No datacenter overlap - good design!");
}

// Calculate uptime percentage for a 48-hour window
var window = interval(0, 48);
var totalUptime = actualUptime.totalDuration(window);
var windowDuration = Duration.ofHours(48);
var uptimePercentage = (totalUptime.toMinutes() * 100.0) / windowDuration.toMinutes();
System.out.printf("Uptime: %.2f%%\n", uptimePercentage);
// Output: Uptime: 95.83% (46 hours out of 48)
```

**Why this matters:** These operations implement set algebra on interval collections, allowing complex temporal logic to be expressed as composable operations. The expression `(Primary OR Failover) AND NOT Maintenance` translates directly to: `union(P, F) ∩ complement(M)`. The operations follow boolean algebra axioms (associativity, commutativity, De Morgan's laws), operating on interval sets rather than binary values. No manual interval arithmetic, no edge case handling—the operators handle merging, splitting, and complementing automatically. This is classical set theory applied to temporal data.

**Performance & Implementation: Lazy Views, Not Eager Computation**

Here's the key insight: when you use `IntervalCollections.and()`, `.or()`, or `.not()`, you're not creating a new collection with precalculated results. Instead, you get a **view**—a lazy wrapper that computes results on-demand.

```java
// This doesn't calculate anything yet - just creates a view
var actualUptime = IntervalCollections.and(
    combinedDatacenters,
    IntervalCollections.not(maintenanceWindows)
);

// NOW computation happens - on-the-fly as you iterate
actualUptime.intervals().forEach(System.out::println);

// Each query recalculates from source collections
var firstSlot = actualUptime.find(window, FORWARD, Duration.ofHours(2));   // Fresh calculation
var count = actualUptime.intervals().count();                              // Another calculation
```

**Why this design?**

1. **Memory efficiency**: No intermediate storage. Complex boolean expressions don't create copies of interval data.

2. **Always fresh**: Changes to source `IntervalSet` instances are immediately visible through views. No stale data.

3. **Selective computation**: Only requested intervals are computed. Query a small window in a 48-hour period? Only that window gets processed.

4. **TreeMap foundation**: `IntervalSet` is backed by `TreeMap` (Java's red-black tree), enabling:
   - O(log n) lookup for interval boundaries
   - Efficient range queries (only relevant intervals examined)
   - Natural ordering maintains temporal sequence

**Performance characteristics:**

```java
// Even with massive collections...
var hugeCollection = IntervalSets.disjoint();
for (int i = 0; i < 10_000; i++) {
    hugeCollection.include(interval(i * 2, i * 2 + 1));  // 10,000 intervals
}

// Querying small windows is still fast
var window = interval(5000, 5010);  // Tiny window in huge collection
var result = hugeCollection.intervals(window, FORWARD);  // O(log n) to find range
// Only intervals overlapping [5000..5010) are examined - not all 10,000!
```

The lazy evaluation model means boolean algebra operations compose efficiently. A complex expression like `and(or(A, B), not(C))` doesn't materialize intermediate results—it streams through source collections computing only what's requested. This is why TesseractLib can handle production workloads with thousands of intervals without choking.

**Trade-off:** If you query the same view repeatedly, consider materializing it into an `IntervalSet`:

```java
// Repeated queries on the same view? Materialize it:
var frequentlyQueried = IntervalSets.disjoint(actualUptime);
// Now queries hit the TreeMap directly, not the lazy view chain
```

### Modifying Sets

`IntervalSet` is **explicitly mutable** (unlike most TesseractLib types):

```java
// Include adds/merges intervals
set.include(interval(20, 24));
set.include(Moments.moment(at(22)), Moments.moment(at(23)));  // Can use Moments

// Exclude removes intervals
set.exclude(interval(10, 14));  // Punches a hole

// Check before modifying
if (!set.isNever()) {  // Not empty
    set.include(interval(0, 5));
}
```

## IntervalCollection

The read-only interface. All query operations, no modifications. Most boolean algebra operations (like `and()`, `or()`, `not()`) return `IntervalCollection`, not `IntervalSet`.

**Key distinction:**

- `IntervalSet` = mutable, stores intervals, has `include()`/`exclude()`
- `IntervalCollection` = immutable interface, query-only operations

> **Good news:** `Interval` also implements `IntervalCollection`, which means your brain doesn't have to context-switch between "working with one interval" and "working with many intervals." Anywhere you need an `IntervalCollection`, just toss in a single `Interval`. Want to find overlap between an interval and a collection? `and(collection, singleInterval)` works. Need to exclude a single maintenance window from a complex schedule? `and(schedule, not(oneInterval))` works. No wrapper classes, no "convert single to collection" helper methods, no StackOverflow searches at 3 AM wondering why the type system is yelling at you. Polymorphism doing what polymorphism was meant to do: making obvious things obvious.
>
> **Mathematical obviousness check:** Yes, this is obvious. One interval is more than zero intervals, so it's a perfectly valid collection (set cardinality = 1). If you're the type who needs to verify this mathematically: |{interval}| = 1 ≥ 0, therefore it's a set. Set theory doesn't care whether your set has one element or a million. The API just reflects this ancient wisdom.


## Querying Operations

`IntervalCollection` provides several query methods for retrieving and searching intervals:

- **`intervals()`** - Iterate all intervals in forward order
- **`intervals(window, direction)`** - Query intervals within a time window (forward/backward)
- **`find(window, direction, duration)`** - Find first interval matching minimum duration
- **`bounds(window, direction)`** - Get interval boundaries as start/end signals
- **`span()`** - Get the overall span covering all intervals
- **`totalDuration(window)`** - Calculate total duration within a window
- **`isNever()`** - Check if collection is empty
- **`isAlways()`** - Check if collection covers all time

### Basic Iteration

```java
// Iterate all intervals in forward order
set.intervals().forEach(interval -> {
    System.out.println("Interval: " + interval);
});

// FluentIterator provides functional operations
var count = set.intervals().count();
var firstInterval = set.intervals().findFirst();
```

### Windowed Queries

Query intervals within a specific time window:

```java
// Find all intervals between 6 AM and 3 PM
var window = interval(6, 15);

// Forward direction (6 AM → 3 PM)
set.intervals(window.forward())
    .forEach(System.out::println);

// Backward direction (3 PM → 6 AM)
set.intervals(window.backward())
    .forEach(System.out::println);
```

**Real-world example:**
```java
// "Show me all meetings today between 9 AM and 5 PM"
var businessDay = interval(9, 17);
var meetings = meetingSet.intervals(businessDay.forward());

// Process in order
meetings.forEach(meeting -> {
    System.out.println("Meeting: " + meeting.getLower() + " to " + meeting.getUpper());
});
```

### Finding Intervals

Search for intervals matching specific criteria:

```java
// Find first interval that can fit a 4-hour block
var slot = set.find(
    interval(0, 24),           // Search window: full day
    QueryDirection.FORWARD,     // Search forward
    Duration.ofHours(4)         // Need 4 hours
);

slot.ifPresent(interval -> {
    System.out.println("Found 4-hour slot: " + interval);
});
```

**Practical use case:**
```java
// "Find me the first available 2-hour slot after 2 PM"
var availableSlot = availability.find(
    Intervals.intervalFrom(at(14)),  // From 2 PM onward
    QueryDirection.FORWARD,
    Duration.ofHours(2)
);

availableSlot.ifPresentOrElse(
    slot -> System.out.println("Book appointment at: " + slot.getLower()),
    () -> System.out.println("No availability found")
);
```

### Boundary Signals

Get start/end points as signals (useful for event processing):

```java
// Get all interval boundaries as signals
set.bounds(interval(0, 24).forward())
    .forEach(signal -> {
        var moment = signal.getAt();
        var entering = signal.getValue();  // true = interval starts, false = interval ends
        System.out.println(moment + ": " + (entering ? "START" : "END"));
    });
```

**Why signals?** They're perfect for sweep-line algorithms or processing interval events in chronological order.

## Collection Properties

Check collection state:

```java
// Is the collection empty?
if (set.isNever()) {
    System.out.println("No intervals");
}

// Does it cover everything?
if (set.isAlways()) {
    System.out.println("Covers all time");
}

// Get the overall span
set.span().ifPresent(span -> {
    System.out.println("Covers " + span.getLower() + " to " + span.getUpper());
});

// Calculate total duration within a window
var totalHours = set.totalDuration(interval(0, 24));
System.out.println("Total coverage: " + totalHours.toHours() + " hours");
```

## Practical Examples

### Example 1: Availability Management

```java
// Build a weekly availability schedule
var availability = IntervalSets.disjoint();

// Add work hours for each day (Mon-Fri, 9-5)
for (int day = 0; day < 5; day++) {
    var dayStart = day * 24 + 9;   // 9 AM
    var dayEnd = day * 24 + 17;    // 5 PM
    availability.include(interval(dayStart, dayEnd));
}

// Remove lunch breaks (12-1 PM each day)
for (int day = 0; day < 5; day++) {
    var lunchStart = day * 24 + 12;
    var lunchEnd = day * 24 + 13;
    availability.exclude(interval(lunchStart, lunchEnd));
}

// Find next 2-hour availability
var nextSlot = availability.find(
    Intervals.intervalFrom(at(0)),
    QueryDirection.FORWARD,
    Duration.ofHours(2)
);

nextSlot.ifPresent(slot -> 
    System.out.println("Next 2-hour window: " + slot)
);
```

### Example 2: Meeting Room Bookings

```java
// Track booked times
var bookings = IntervalSets.disjoint(
    interval(9, 10),    // 9-10 AM meeting
    interval(14, 15),   // 2-3 PM meeting
    interval(15, 16)    // 3-4 PM meeting (auto-merges to 2-4 PM)
);

// Check if specific time is available
var requestedTime = interval(10, 11);
var isAvailable = bookings.intervals(requestedTime.forward())
    .count() == 0;

if (isAvailable) {
    bookings.include(requestedTime);
    System.out.println("Booking confirmed");
}
```

### Example 3: Event Processing with Signals

```java
// Process interval boundaries chronologically
var events = IntervalSets.disjoint(
    interval(9, 12),
    interval(14, 17)
);

var activeCount = 0;
events.bounds(interval(0, 24).forward())
    .forEach(signal -> {
        if (signal.getValue()) {
            activeCount++;
            System.out.println(signal.getAt() + ": Event STARTS (active: " + activeCount + ")");
        } else {
            activeCount--;
            System.out.println(signal.getAt() + ": Event ENDS (active: " + activeCount + ")");
        }
    });
```



## Common Patterns

```java
// Check if time is covered
var isCovered = !set.intervals(interval(14, 15).forward()).isEmpty();

// Get all interval starts
set.bounds(interval(0, 24).forward())
    .filter(signal -> signal.getValue())  // Filter for starts only
    .map(Signal::getAt)
    .forEach(System.out::println);

// Count intervals in a window
var count = set.intervals(interval(9, 17).forward()).count();

// Find largest gap
// (Use boolean algebra with IntervalCollections.not() - see interval-container.md)

// Merge two sets
var combined = IntervalSets.disjoint();
set1.intervals().forEach(combined::include);
set2.intervals().forEach(combined::include);
```

---

**Next Steps:**

- Check out [Temporal Primitives](temporal-primitives.md) for Moment and Interval details
- Explore [Spatial Primitives](spatial-primitives.md) for location-based operations
- Learn about [Presence Collections](presence-collection.md) for spatio-temporal tracking
