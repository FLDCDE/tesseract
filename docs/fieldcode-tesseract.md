# TesseractLib

A high-performance Java library for temporal and spatial data modeling.

## Table of Contents

- [The Problem](#the-problem)
- [The Solution](#the-solution)
- [Current Status](#current-status)
- [Key Features](#key-features)
- [Project Structure](#project-structure)
- [When to Use TesseractLib](#when-to-use-tesseractlib)
- [Getting Your Hands Dirty](#getting-your-hands-dirty)
  - [Basic Interval Creation](#basic-interval-creation)
  - [Documentation Notation](#documentation-notation)
  - [Real-Life Example](#real-life-example-temporal-office-availability)
- [Documentation](#documentation)
- [License](#license)

---

## The Problem

We built a ticketing system where appointments are events on people's timelines. Standard Java collections made interval operations surprisingly 
painful—questions like "find conflicting appointments", "merge adjacent time slots", "calculate overlapping schedules" or "query availability windows" required complex, 
error-prone code every time. (Turns out `List<Appointment>` doesn't have a `.magicallyResolveOverlappingTimeSlots()` method. We checked. Twice.)
We found ourselves repeatedly solving the same temporal and spatial problems: resource availability across timezones, timeline intersections, 
overlapping schedules, spatial-temporal queries at scale, and fetching distance and duration between two locations. 
It felt less like software engineering and more like manually implementing a calendar app using only `if` statements and existential dread.

## The Solution

TesseractLib provides APIs for temporal and spatial data operations. The library includes:

- **Temporal primitives**: Moments (points in time) and intervals (time ranges) with operations for comparison, merging, and querying
- **Spatial primitives**: Locations (lat/long coordinates), distances, directions, and presence tracking (moment + location pairs)
- **Collections**: Efficient storage and querying for intervals and spatio-temporal data
- **Boolean algebra**: Combine intervals using set operations (`and`, `or`, `not`)—because apparently calendar math needed its own branch of mathematics

Common use cases include scheduling systems, resource availability tracking, time series analysis, and geospatial queries.

The library prioritizes performance over flexibility. Intervals use closed-open ranges `[start..end)` with `OffsetDateTime` endpoints—no configurable types or pluggable representations. This constraint enables faster operations and simpler algorithms. (Translation: we made choices so you don't have to argue about them in code reviews.) All temporal data is timezone-aware via `OffsetDateTime`.

## Current Status

> **Fair warning:** While TesseractLib runs our production systems daily, it's not uniformly polished. Interval operations? Battle-tested and solid. Other parts like `IntervalMap`? Let's call them "functional but due for refinement." We built what we needed, prioritized what mattered most, and now we're open-sourcing the whole thing—rough edges included. Also worth noting: we use Jackson exclusively for JSON, so that's the only serialization integration available. Need Gson or Protocol Buffers? You'll need to build your own adapters. Think of this as production-proven software that's still evolving. Contributions welcome!

> **About the name:** We evolved from "fieldcode-ts" (temporal-spatial) to Tesseract—because a 4D hypercube beats an acronym any day. The name fits perfectly: 3D for spatial, 1D for temporal. Yes, location is technically 2D, but we're keeping the name. (And yes, there's another tesseract library in a different domain—great minds think alike. To be fair, we originally developed this for internal use only, so picking a unique name wasn't our highest priority. To distinguish between them, we always use "Fieldcode-Tesseract" when referring to this library.)

## Key Features

**Temporal Primitives**

- **Moment**: Precise points in time with infinite boundary support. Built on `OffsetDateTime` with comparison, shifting, and arithmetic operations.
- **Interval**: Time spans with merging, querying, and algebraic operations. Notation: `interval(5, 16)` creates 5:00–16:00 UTC.

**Spatial Primitives**

- **Location**: Geographic positions defined by latitude and longitude with distance calculations and spatial queries.
- **Presence**: Fusion of time and space—combines a `Moment` with a `Location` for spatio-temporal tracking.
- **Distance**: Measurements between locations with multiple unit support (meters, kilometers, miles).
- **Direction**: Directional relationships and movements between locations with bearing calculations.

**Collections & Containers**

- **Interval Collections**: Efficiently store, merge, and query time intervals.
- **Interval Containers**: Manage multiple interval sets with tags for complex scheduling workflows.
- **IntervalMap**: Associate values with time intervals, enabling lookups like "what was the temperature at 3 PM?" or "who was on-call during this period?"
- **PresenceSet**: Store and retrieve non-overlapping spatio-temporal data points (moment + location combinations) with efficient querying.

**Advanced Capabilities**

- **Lazy Iterators**: High-performance navigation and transformation without loading entire datasets.
- **Temporal Boolean Algebra**: Combine intervals with `and()`, `or()`, `not()` operations.
- **Extensible Architecture**: Modular design for custom extensions and integrations.

> **Note:** All temporal data uses Java's `OffsetDateTime` for timezone-aware precision.

## Project Structure

TesseractLib is organized into focused modules for flexibility and minimal dependencies:

- **tesseract-api** — Core interfaces and contracts. Defines the public API without implementations. Use this for compile-time dependencies when you don't need the full library.
- **tesseract-core** — Complete implementations of all temporal and spatial features. This is what you'll typically depend on for production use.
- **tesseract-jackson-datatype** — Jackson serialization/deserialization support for all Tesseract types. Add this for JSON integration.

**Maven coordinates:**
```xml
<!-- Core library (most common) -->
<dependency>
  <groupId>com.fieldcode</groupId>
  <artifactId>tesseract-core</artifactId>
  <version>0.1.0</version>
</dependency>

<!-- Jackson support -->
<dependency>
  <groupId>com.fieldcode</groupId>
  <artifactId>tesseract-jackson-datatype</artifactId>
  <version>0.1.0</version>
</dependency>
```

## When to Use TesseractLib

TesseractLib shines in applications that need sophisticated temporal and spatial data handling. Here are common scenarios where it provides significant value:

### High-Level Use Cases

**Scheduling & Calendar Systems**

- Employee shift management with overlapping schedules
- Meeting room booking and conflict detection
- Appointment scheduling with availability windows
- Calendar synchronization across timezones

**Resource Management**

- Equipment rental and availability tracking
- Vehicle fleet scheduling and routing
- Technician dispatching with time and location constraints
- Capacity planning with temporal constraints

**Time Series & Analytics**

- Event stream processing with temporal windows
- Log aggregation and time-based queries
- Performance monitoring with time ranges
- Historical data analysis with interval-based filtering

**Location-Based Services**

- Delivery route optimization with time windows
- Geofencing with temporal activation periods
- Asset tracking with spatio-temporal queries
- Field service management combining location and schedule

**Business Logic**

- SLA tracking with service windows
- Billing systems with time-based rate changes
- Workflow management with temporal dependencies
- Access control with time-limited permissions

### Low-Level Programming Challenges

TesseractLib solves common temporal programming problems:

- Checking if two time periods overlap (yes, even the ones that kiss at midnight and pretend nothing happened)
- Merging adjacent or overlapping time slots
- Finding gaps between intervals (because apparently, time has holes)
- Calculating the union or intersection of multiple time ranges
- Determining if a specific moment falls within a time window
- Splitting intervals into smaller chunks
- Handling infinite or unbounded time ranges (because sometimes "until the heat death of the universe" is a valid requirement)
- Performing set operations (AND, OR, NOT) on time intervals
- Iterating through time periods without loading everything into memory
- Finding the next/previous available time slot matching criteria

### Red Flags You Need TesseractLib

You'll know it's time to add TesseractLib when you find yourself:

- Writing nested loops comparing dates and times
- Repeatedly implementing the same "does this overlap?" logic
- Struggling with interval comparison edge cases
- Needing to optimize temporal queries for performance
- Reinventing interval trees or segment trees (we've all been there—it's the programmer's equivalent of building IKEA furniture without instructions)
- Dealing with complex timezone-aware temporal operations
- Writing increasingly complex date/time logic that's hard to maintain

**Bottom line:** If you're writing complex date/time logic, managing overlapping intervals, or combining temporal and spatial queries, TesseractLib will simplify your code significantly.

## Getting Your Hands Dirty

This section covers interval operations and temporal boolean algebra—one of the library's core capabilities. These examples represent a fraction of what's available. The library also includes spatial primitives, presence tracking, distance calculations, direction matrices, and more advanced querying features. For those topics, check the other documentation pages or explore the source code directly.

### Basic Interval Creation

Create intervals using `Intervals.interval()` with `OffsetDateTime` start and end times:

```java
import com.fieldcode.tesseract.interval.Intervals;
import java.time.OffsetDateTime;

// Valid for standard business hours: 9:00–17:00
var permitValidityWindow = interval(9, 17);

// Check if current time falls within permit validity
var now = OffsetDateTime.now();
if (permitValidityWindow.contains(now)) {
    System.out.println("✓ Permit valid. Time travel authorized.");
} else {
    System.out.println("✗ Permit expired. File form TPO-1471-B for renewal.");
}
```

### Documentation Notation

> **Examples notation:** Throughout this document, we use helper methods like `moment(14)` and `interval(9, 17)` as shorthand. These create temporal objects relative to today's beginning in UTC. For example, `interval(9, 17)` means [9:00..17:00) UTC today. For complete implementation details, see [Helper Notation](helper-notation.md).

### Real-Life Example: Temporal Office Availability

The Temporal Permit Office posts business hours, but actual citizen access requires accounting for lunch breaks and audits. This example shows how to calculate real availability using temporal boolean algebra—combining interval sets with `and()`, `or()`, and `not()` operations.

```java
import static com.fieldcode.tesseract.set.IntervalSets.*;
import static com.fieldcode.tesseract.collection.IntervalCollections.*;
import java.time.Duration;

// Define 5-day work week: 9:00-18:00 daily
var officeHours = IntervalSets.disjoint(
    interval(9, 18),
    interval(9, 18).shift(Duration.ofDays(1)),
    interval(9, 18).shift(Duration.ofDays(2)),
    interval(9, 18).shift(Duration.ofDays(3)),
    interval(9, 18).shift(Duration.ofDays(4))
);

// Lunch breaks: 13:00-14:00 daily (mandatory paradox resolution—requires quorum)
var paradoxResolution = IntervalSets.disjoint(
    interval(13, 14),
    interval(13, 14).shift(Duration.ofDays(1)),
    interval(13, 14).shift(Duration.ofDays(2)),
    interval(13, 14).shift(Duration.ofDays(3)),
    interval(13, 14).shift(Duration.ofDays(4))
);

// Timeline audits: Day 0 afternoon (15:00-18:00) and all of Day 2
var audits = IntervalSets.disjoint(
    interval(15, 18),
    interval(9, 18).shift(Duration.ofDays(2))
);

// Boolean algebra: office hours AND NOT (lunch OR audits)
var availability = and(officeHours, not(or(paradoxResolution, audits)));

// Display results
System.out.println("Temporal Permit Office - Access Windows:");
availability.intervals().forEach(window -> 
    System.out.println("  ✓ " + window)
);
```

**Sample output:**
```
Temporal Permit Office - Access Windows:
  ✓ [2025-11-06T09:00Z..2025-11-06T13:00Z)
  ✓ [2025-11-06T14:00Z..2025-11-06T15:00Z)
  ✓ [2025-11-07T09:00Z..2025-11-07T13:00Z)
  ✓ [2025-11-07T14:00Z..2025-11-07T18:00Z)
  ...
```

**Finding continuous time windows:**

```java
// Form TPO-2083 requires 4 uninterrupted hours to process
var processingWindow = availability.find(
    Intervals.always(),           // Search all time
    QueryDirection.BACKWARD,      // Start from latest
    Duration.ofHours(4)           // Minimum duration
);

processingWindow.ifPresentOrElse(
    window -> System.out.println("✓ Can process form during: " + window),
    () -> System.out.println("✗ No continuous 4-hour window found (submit form TPO-9999 for overtime approval)")
);
// Output: ✓ Can process form during: [2025-11-07T14:00Z..2025-11-07T18:00Z)
```

**Key techniques demonstrated:**

- **Temporal boolean algebra**: Combining interval collections with `and()`, `or()`, `not()`
- **Multi-day scheduling**: Building week schedules using `shift(Duration)`
- **Set subtraction**: Computing availability by excluding closure periods
- **Duration-based queries**: Finding windows that meet minimum time requirements
- **Lazy evaluation**: Operations compute results on-demand without materializing intermediate sets

## Documentation

Explore feature-specific guides with examples:

- [Temporal Primitives](temporal-primitives.md) — Moments and intervals in depth
- [Spatial Primitives](spatial-primitives.md) — Locations, distances, directions, and movements
- [Interval Collection](interval-collection.md) — Efficient interval storage and querying
- [Presence Collection](presence-collection.md) — Spatio-temporal data tracking and querying
- [Helper Notation](helper-notation.md) — Test helpers and documentation shortcuts

## License

TesseractLib is open source under the MIT License.

---

