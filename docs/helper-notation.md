# Helper Notation

Throughout TesseractLib documentation, we use helper methods to create compact, readable examples. This pattern is commonly used in tests and documentation where you need to generate temporal or spatial objects quickly.

## Interval Shorthand

Instead of verbose `OffsetDateTime` construction:

```java
// Verbose
var start = OffsetDateTime.now().withHour(9).withMinute(0);
var end = OffsetDateTime.now().withHour(17).withMinute(0);
var workday = Intervals.interval(start, end);
```

We write:

```java
// Compact
interval(9, 17)  // Means 9:00-17:00 UTC today
```

## Implementation

The helper methods establish a fixed reference point (today's beginning in UTC) and build temporal objects relative to it, making examples predictable and easy to understand.

```java
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.interval.Intervals;
import com.fieldcode.tesseract.moment.Moments;

public class TimeTestSupport {
    private static final OffsetDateTime reference = OffsetDateTime.now()
        .withOffsetSameLocal(ZoneOffset.UTC)
        .truncatedTo(ChronoUnit.DAYS);

    public static OffsetDateTime at(int hours) {
        return reference.plusHours(hours);
    }

    public static Moment moment(int hours) {
        return Moments.moment(at(hours));
    }

    public static Interval interval(int fromHours, int toHours) {
        return Intervals.interval(at(fromHours), at(toHours));
    }
}
```

## Helper Methods Explained

The `TimeTestSupport` class provides several helper methods that work together:

### `at(int hours)`

Creates an `OffsetDateTime` for a specific hour of today in UTC.

```java
var nineAM = at(9);   // Today at 9:00 UTC
var noon = at(12);    // Today at 12:00 UTC
var midnight = at(0); // Today at 00:00 UTC
```

**What it does:** Takes the reference point (today at midnight UTC) and adds the specified number of hours. This is the foundation method that all other helpers use.

### `moment(int hours)`

Creates a `Moment` for a specific hour of today in UTC.

```java
var morningMoment = moment(9);   // Moment at 9:00 UTC today
var afternoonMoment = moment(14); // Moment at 14:00 UTC today
```

**What it does:** Calls `at(hours)` to get an `OffsetDateTime`, then wraps it in a `Moment` using `Moments.moment()`.

### `interval(int fromHours, int toHours)`

Creates an `Interval` spanning from one hour to another today in UTC.

```java
var workday = interval(9, 17);      // [9:00..17:00) UTC today
var morning = interval(6, 12);      // [6:00..12:00) UTC today
var lateNight = interval(22, 24);   // [22:00..24:00) UTC today
```

**What it does:** Calls `at(fromHours)` and `at(toHours)` to get start and end times, then creates an interval using `Intervals.interval()`. Remember: intervals are half-open `[start..end)`.

## Extending for Spatial Objects

You can extend this pattern for spatial objects:

```java
import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.location.Locations;
import com.fieldcode.tesseract.presence.Presences;

public class TimeTestSupport {
    // ...existing temporal helpers...

    public static Location location(double lat, double lon) {
        return Locations.location(lat, lon);
    }

    public static Presence presence(int hours, double lat, double lon) {
        return Presences.presence(moment(hours), location(lat, lon));
    }
}
```

## Usage in Tests

Create a `TimeTestSupport` base class or utility class in your test suites. This keeps test code clean and focused on logic rather than verbose object construction.

```java
public class MyTest extends TimeTestSupport {
    @Test
    public void testAvailability() {
        var availability = IntervalSets.disjoint(
            interval(9, 12),   // 9 AM - 12 PM
            interval(13, 17)   // 1 PM - 5 PM
        );
        
        // Test logic here...
    }
}
```

## Why This Pattern?

**Benefits:**
- **Readability**: `interval(9, 17)` is clearer than constructing `OffsetDateTime` objects
- **Consistency**: All examples use the same reference point (today at UTC midnight)
- **Maintainability**: Change the reference logic in one place
- **Focus**: Tests and examples focus on logic, not boilerplate

**Common in tests:** This pattern works especially well for temporal-spatial scenarios where you need to generate many example objects with readable, predictable values.

---

**Related:**
- [Temporal Primitives](temporal-primitives.md) - Moment and Interval basics
- [Interval Collections](interval-collection.md) - Working with sets of intervals

