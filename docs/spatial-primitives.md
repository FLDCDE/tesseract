# Spatial Primitives

Core building blocks for working with space: **Location** (a point on Earth), **Distance** (separation between points), **Presence** (a moment + location fusion), **Movement** (transition between presences), **Direction** (origin-destination pairing), **DirectionFeature** (direction with routing metadata), and **Track** (journey with distance).

---

## TL;DR

**Location** = A geographic point defined by latitude and longitude. Immutable, comparable, and includes a special `anywhere()` singleton for "location doesn't matter" scenarios (useful when Temporal Permit Office regulations only care about *when*, not *where*). Can parse location codes and calculate distances between TPO branches.

**Distance** = Separation between locations. Supports meters, kilometers, miles, and centimeters. Immutable, comparable, and can be added together. Essential for calculating travel reimbursements when Field Agent Martinez files form TPO-7702 ("Expenses Incurred During Temporal Investigation").

**Presence** = A spatio-temporal event combining `Moment` (when) and `Location` (where). Immutable, shiftable, and perfect for tracking "Field Agent Martinez filed form TPO-1471 at Budapest HQ at 14:00 UTC." The marriage of time and space—required for all temporal audit trails.

**Movement** = A spatio-temporal transition from one `Presence` to another. Represents "Agent moved from Budapest HQ at 9:00 to London Branch at 12:00" without specifying distance. Perfect when you know where and when someone was at two points, but distance isn't relevant. Like Track's simpler sibling—same temporal interval, no distance baggage.

**Direction** = A simple pairing of origin and destination locations. The most basic way to express "from here to there." Think "Budapest HQ → London Branch" (no distance, no duration, just the endpoints). Topology only—the Filing Department uses this for routing paperwork between offices.

**DirectionFeature** = Direction with metadata—adds `Distance` and `Duration` to the origin-destination pair. Represents real-world routing information fetched from external APIs: "Budapest to London: 1,445 km, 2.5 hours by flight." Logistics Coordinator uses this for agent deployment planning.

**Track** = A spatio-temporal journey combining two `Presence` objects (origin and destination) with a `Distance`. Represents complete field agent movement: "Agent Martinez departed Budapest HQ at 9:00, arrived London Branch at 12:00, covering 1,445 km." The complete audit trail required by Temporal Compliance Officer Jenkins.

**Factory pattern:** `Location` → `Locations`, `Distance` → `Distances`, `Presence` → `Presences`, `Movement` → `Movements`, `Direction` → `Directions`, `Track` → `Tracks`. Add an 's'. Still working.

---

## Table of Contents

- [Foundation](#foundation)
- [Location](#location)
  - [Creating Locations](#creating-locations)
  - [Implementation Types](#implementation-types)
  - [Location Operations](#location-operations)
- [Distance](#distance)
  - [Creating Distances](#creating-distances)
  - [Distance Operations](#distance-operations)
- [Presence](#presence)
  - [Creating Presences](#creating-presences)
  - [Presence Operations](#presence-operations)
- [Movement](#movement)
  - [Creating Movements](#creating-movements)
  - [Movement Operations](#movement-operations)
- [Direction](#direction)
  - [Creating Directions](#creating-directions)
  - [Direction Operations](#direction-operations)
- [DirectionFeature](#directionfeature)
  - [Creating DirectionFeatures](#creating-directionfeatures)
  - [DirectionFeature Operations](#directionfeature-operations)
- [Track](#track)
  - [Creating Tracks](#creating-tracks)
  - [Track Operations](#track-operations)
- [Practical Example](#practical-example)
- [Next Steps](#next-steps)

---

## Foundation

> **Foundation:** All spatial data uses standard WGS84 coordinates (latitude/longitude). Distances use meters as the base unit with conversions to kilometers, miles, and centimeters.  
> **Immutability:** All spatial primitives (Location, Distance, Presence, Movement, Direction, DirectionFeature, Track) are immutable. Operations return new instances, never modify originals. Thread-safe by default.  
> **Integration:** Spatial primitives integrate seamlessly with temporal primitives—`Presence` combines `Moment` and `Location`, `Movement` and `Track` combine two `Presence` objects for complete spatio-temporal modeling.

> **Examples notation:** Throughout this document, we use helper methods like `location(lat, lon)`, `moment(hours)`, `at(hours)`, and `interval(from, to)` as shorthand. These create temporal and spatial objects relative to standard conventions (today's UTC midnight as reference point). For complete implementation details, see [Helper Notation](helper-notation.md).

---

## Location

A geographic point defined by latitude and longitude coordinates. Built for precision, comparison, and distance calculations.

> **Historical Note:** Location currently stores latitude and longitude as `double` primitives internally. Yes, we know `BigDecimal` would be more appropriate for precise geographic coordinates—the Temporal Permit Office's Cartography Department files a complaint about this every quarter (form TPO-GEO-001: "Request for Precision Enhancement"). We inherited this design from the early days when performance mattered more than the ability to pinpoint coordinates to sub-millimeter accuracy. It's on the roadmap for a future version, right after we finish processing the backlog of temporal permit applications filed in 1847. For now, double precision is accurate enough to distinguish between "Budapest HQ" and "the coffee shop next door," which satisfies 99.9% of our use cases. If you need to track the exact position of individual atoms during time travel, please file form TPO-QUANTUM-042 and wait 3-5 business centuries.

**Factory pattern:** In TesseractLib, spatial primitives follow the same convention as temporal ones—add an 's' to get the factory class. `Location` → `Locations`, `Distance` → `Distances`, `Presence` → `Presences`, `Movement` → `Movements`, `Direction` → `Directions`, `Track` → `Tracks`. Simple, consistent, impossible to forget (except when you do).

### Creating Locations

```java
import com.fieldcode.tesseract.location.Locations;

// Standard geographic coordinates - Temporal Permit Office branches
var budapestHQ = Locations.location(47.4979, 19.0402);        // Latitude, Longitude
var londonBranch = Locations.location(51.5074, -0.1278);      // UK branch
var newYorkBranch = Locations.location(40.7128, -74.0060);    // US branch

// Parse from location code string (custom format)
var parsedLocation = Locations.parse("47.4979,19.0402");      // "lat,lon" format

// Special singleton - "location doesn't matter"
var anywhere = Locations.anywhere();                          // For permits valid at any location
```

> **Fun fact:** The Temporal Permit Office originally tried to use the North Pole as a "default location" for permits without location restrictions. This caused three incidents where time-traveling penguins filed confused appeals. The `anywhere()` singleton solved this philosophical conundrum cleanly—no arbitrary coordinates, no confused wildlife.

### Implementation Types

TesseractLib provides two location implementations optimized for different scenarios:

1. **Standard Location** — A specific geographic point
   - Defined by latitude (±90°) and longitude (±180°)
   - Immutable and thread-safe
   - Supports distance calculations using Haversine formula
   - Example: Budapest HQ at `47.4979°N, 19.0402°E`
   - Location code is auto-generated in `"lat,lon"` format (e.g., `"47.4979,19.0402"`)
   - Implementation: `ImmutableLocation` (generated by Immutables library)

2. **Anywhere Location** — No specific location (**singleton**)
   - Represents "location-independent" scenarios
   - Always compares equal to itself, but not to specific locations
   - Distance calculations with `anywhere()` return zero distance (philosophical: distance to "anywhere" is meaningless)
   - **Singleton**: Only one instance exists application-wide (`Locations.anywhere()`)
   - Example: Universal temporal permits valid at any geographic location
   - Implementation: `ImmutableLocationAnywhere` (generated singleton by Immutables library)


### Location Operations

**Comparisons:**

```java
// Two Temporal Permit Office branches
var budapestHQ = location(47.4979, 19.0402);
var londonBranch = location(51.5074, -0.1278);

// Locations implement Comparable<Location>
budapestHQ.compareTo(londonBranch);  // Returns ordering (by lat, then lon)

// Equality checks
budapestHQ.equals(londonBranch);     // false - different coordinates
budapestHQ.equals(location(47.4979, 19.0402));  // true - same coordinates
```

**Properties:**

```java
var office = location(47.4979, 19.0402);

// Extract coordinates
var lat = office.getLatitude();      // 47.4979
var lon = office.getLongitude();     // 19.0402

// Check if this is the anywhere singleton
var isGeneric = office.isAnywhere(); // false (specific location)
var anywhere = Locations.anywhere();
anywhere.isAnywhere();               // true (singleton)

// Convert to list format [lon, lat] (GeoJSON order)
var coords = office.asLonLatList();  // [19.0402, 47.4979]

// String representation (auto-generated from coordinates)
var locationString = office.toString();  // "47.4979,19.0402" (lat,lon format)
```

---

## Distance

A measurement of separation between two locations. Supports multiple units and arithmetic operations.

> **Historical Note:** The `Distance` primitive was born when someone stared at an API endpoint response containing a `distance` property and asked "Wait, is this meters? Kilometers? Miles? Parsecs?" Nobody knew. The API documentation, naturally, said nothing. Rather than decode ancient endpoint specifications (filed somewhere between "Digitize 1800s Forms" and "Document Literally Anything"), we built `Distance` to explicitly wrap the value with its unit. Yes, modeling it like `java.time.Duration` would be architecturally superior, but we needed it *yesterday* and architectural purity doesn't ship features. It works, it's clear, and nobody has to guess whether `1000` means "one kilometer" or "one thousand millimeters."

### Creating Distances

```java
import com.fieldcode.tesseract.distance.Distances;
import com.fieldcode.tesseract.DistanceUnit;

// Create distances in various units
var walkingDistance = Distances.ofMeters(500);              // 500 meters
var drivingDistance = Distances.ofKilometers(15);           // 15 kilometers
var flightDistance = Distances.ofMile(200);                 // 200 miles

// Generic factory with explicit units
var customDistance = Distances.distance(1000, DistanceUnit.METER);

// Predefined constants
var oneMeter = Distances.oneMeter();                        // Exactly 1 meter
var oneKilometer = Distances.oneKilometer();                // Exactly 1000 meters
var noDistance = Distances.zero();                          // 0 meters

// Calculate distance between locations
var budapestHQ = location(47.4979, 19.0402);
var londonBranch = location(51.5074, -0.1278);
var separation = budapestHQ.distanceTo(londonBranch);      // Haversine distance
```

### Distance Operations

**Unit Conversions:**

```java
var distance = Distances.ofKilometers(5);  // 5 km

// Convert to different units
var meters = distance.toMeters();           // 5000
var kilometers = distance.toKilometers();   // 5
var miles = distance.toMiles();             // ~3 (approximate conversion)
var centimeters = distance.toCentimeters(); // 500000
```

**Arithmetic:**

```java
// Field Agent Martinez's daily route (for expense report form TPO-7702)
var morningRoute = Distances.ofKilometers(8);     // TPO Budapest HQ to Investigation Site A: 8 km
var afternoonRoute = Distances.ofKilometers(12);  // Site A to Paradox Zone B: 12 km

// Total distance traveled (required for reimbursement calculation)
var totalDistance = morningRoute.plus(afternoonRoute);  // 20 km total for the day
// Temporal Compliance Officer Jenkins requires accurate distance reporting in triplicate
```

**Comparisons:**

```java
var shortDistance = Distances.ofMeters(500);
var longDistance = Distances.ofKilometers(2);

// Distance implements Comparable<Distance>
shortDistance.lt(longDistance);     // true - less than
shortDistance.le(longDistance);     // true - less than or equal
shortDistance.gt(longDistance);     // false - not greater than
shortDistance.ge(longDistance);     // false - not greater than or equal
shortDistance.eq(longDistance);     // false - not equal
shortDistance.ne(longDistance);     // true - not equal

// Natural ordering
shortDistance.compareTo(longDistance);  // negative (short < long)
```

---

## Presence

A spatio-temporal event combining a `Moment` (when) and a `Location` (where). Perfect for tracking "Field Agent Martinez was at Budapest HQ at 14:00."

### Creating Presences

```java
import com.fieldcode.tesseract.presence.Presences;
import com.fieldcode.tesseract.moment.Moments;
import com.fieldcode.tesseract.tag.Tags;
import java.time.OffsetDateTime;

// Create presence - Field Agent Martinez checking in at TPO Budapest HQ
var when = Moments.now();
var where = Locations.location(47.4979, 19.0402);  // Budapest HQ coordinates
var agentCheckIn = Presences.presence(when, where);
// Used for form TPO-3001 ("Field Agent Location Verification Report")

// Convenience: create with OffsetDateTime directly
var checkIn = Presences.presence(
    OffsetDateTime.now(),
    Locations.location(47.4979, 19.0402)
);

// Convenience: create with lat/lon directly (for rapid filing)
var siteVisit = Presences.presence(
    Moments.now(),
    47.4979,
    19.0402  // Investigation site coordinates
);

// Parse from location code string (legacy form format compatibility)
var parsedPresence = Presences.presence(
    Moments.now(),
    "47.4979,19.0402"  // Format from old TPO-2000 series forms
);

// With tags (for Temporal Compliance classification)
var tags = Tags.builder()
    .tag("field-investigation")
    .tag("paradox-zone")
    .tag("priority", "high")  // Key-value tag for urgency level
    .build();

var taggedPresence = Presences.presence(when, where, tags);
// Flagged for Paradox Administrator Chen's review queue
```

### Presence Operations

**Querying:**

```java
// Extract components
var agentPresence = presence(moment(14), location(47.4979, 19.0402));

var when = agentPresence.getMoment();       // moment(14) - when agent was there
var where = agentPresence.getLocation();    // location(47.4979, 19.0402) - where agent was
```

**Transformation (Immutable):**

```java
// Field Agent Martinez's audit trail (form TPO-3001 requires all location updates)
var morningCheckIn = presence(moment(9), location(47.4979, 19.0402));  // Budapest HQ

// Shift time - agent remained at Budapest HQ but time advanced 2 hours
var laterCheckpoint = morningCheckIn.shift(Duration.ofHours(2));
// Result: presence(moment(11), location(47.4979, 19.0402))
// Used when correcting timestamp errors on filed forms

// Move to different time - update moment directly (temporal adjustment)
var afternoonCheckIn = morningCheckIn.move(moment(14));
// Result: presence(moment(14), location(47.4979, 19.0402))
// Temporal Compliance Officer Jenkins approved time correction via form TPO-8888

// Change moment - same as move()
var newTiming = morningCheckIn.withMoment(moment(15));

// Change location - agent transferred to different TPO branch
var londonVisit = morningCheckIn.withLocation(location(51.5074, -0.1278));
// Result: presence(moment(9), location(51.5074, -0.1278))
// Location reassignment requires supervisor approval stamp
```

**Comparison:**

```java
// Presences implement Comparable<Presence>
// Sorted by moment first, then location
var early = presence(moment(9), location(47.4979, 19.0402));
var late = presence(moment(14), location(47.4979, 19.0402));

early.compareTo(late);  // negative (earlier moment)
```

---

## Movement

A spatio-temporal transition between two `Presence` objects. Represents movement from one location-time point to another without specifying distance. Perfect for tracking "where and when someone was" at two points without caring about the path or distance traveled.

**Factory pattern:** Use `Movements.movement()` to create Movement instances from two presences.

### Creating Movements

```java
import com.fieldcode.tesseract.movement.Movements;

// Method 1: From two presences (most common)
var departure = presence(moment(9), location(47.4979, 19.0402));   // Budapest HQ at 9:00
var arrival = presence(moment(12), location(51.5074, -0.1278));    // London Branch at 12:00

var agentMovement = Movements.movement(departure, arrival);
// Represents: Agent moved from Budapest at 9:00 to London at 12:00
// No distance information - just the two spatio-temporal points

// Method 2: From moments and locations directly
var quickMovement = Movements.movement(
    moment(9), location(47.4979, 19.0402),    // Start: when and where
    moment(12), location(51.5074, -0.1278)    // End: when and where
);
// Convenient when you have raw temporal and spatial data
```

**Use cases:**
- **Location tracking without distance**: "Agent was at Budapest at 9:00, then at London at 12:00" (form TPO-3002: "Agent Location Transitions")
- **Simplified audit trails**: When you only need to know endpoints, not the journey details
- **Database records**: Storing location changes without calculating distances
- **Timeline visualization**: Plotting agent positions over time without route information

### Movement Operations

**Querying:**

```java
var movement = Movements.movement(
    presence(moment(9), location(47.4979, 19.0402)),
    presence(moment(12), location(51.5074, -0.1278))
);

// Access presence endpoints
var origin = movement.getOrigin();           // presence(moment(9), Budapest)
var destination = movement.getDestination(); // presence(moment(12), London)

// Get temporal interval (when the movement occurred)
var interval = movement.getInterval();       // interval(9, 12) - 3 hour span
```

**Transformation (Immutable):**

```java
// Adjust movement timing without changing locations
var originalMovement = Movements.movement(
    moment(9), location(47.4979, 19.0402),
    moment(12), location(51.5074, -0.1278)
);

// Shift departure time to 10:00 (keeping destination time and locations same)
var delayedDeparture = originalMovement.withLower(moment(10));
// Result: Movement from Budapest at 10:00 to London at 12:00

// Shift arrival time to 14:00 (keeping departure time and locations same)
var delayedArrival = originalMovement.withUpper(moment(14));
// Result: Movement from Budapest at 9:00 to London at 14:00
```

**Comparison with Track:**

```java
// Movement: knows WHERE and WHEN, not HOW FAR
var movement = Movements.movement(
    presence(moment(9), location(47.4979, 19.0402)),
    presence(moment(12), location(51.5074, -0.1278))
);
// "Agent was at Budapest at 9:00, then at London at 12:00"

// Track: knows WHERE, WHEN, and HOW FAR
var track = Tracks.track(
    presence(moment(9), location(47.4979, 19.0402)),
    presence(moment(12), location(51.5074, -0.1278)),
    Distances.ofKilometers(1445)
);
// "Agent traveled from Budapest at 9:00 to London at 12:00, covering 1,445 km"

// Use Movement when: distance doesn't matter (location logs, simple transitions)
// Use Track when: distance matters (expense reports, travel planning, route optimization)
```

> **Design Note:** Movement is Track's simpler sibling—same spatio-temporal interval, but without the distance component. Use Movement when you're tracking position changes without caring about the journey's distance. The Temporal Permit Office uses Movement for form TPO-3002 ("Agent Location Transitions") which only requires knowing where agents were at different times, not how far they traveled. For expense reports (form TPO-7702), Track is required because reimbursement depends on distance.

---

## Direction

A simple pairing of origin and destination locations. The most lightweight way to represent "from here to there"—no distance, no duration, just two points.

### Creating Directions

```java
import com.fieldcode.tesseract.direction.Directions;

// Create direction between two Temporal Permit Office branches
// Filing Department uses this to route form TPO-1471 for approval chain
var budapestHQ = location(47.4979, 19.0402);
var londonBranch = location(51.5074, -0.1278);

var route = Directions.direction(budapestHQ, londonBranch);
// Represents: Budapest HQ → London Branch (topology only, no distance/duration)
// The paperwork flows this way, eventually
```

### Direction Operations

**Querying:**

```java
// Extract endpoints
var route = Directions.direction(
    location(47.4979, 19.0402),  // Budapest
    location(51.5074, -0.1278)    // London
);

var origin = route.getOrigin();           // location(47.4979, 19.0402)
var destination = route.getDestination(); // location(51.5074, -0.1278)
```

**Generating All Directions:**

```java
// Paradox Administrator Chen needs routing matrix for form distribution network
// Form TPO-1471 must flow through all branch offices for approval stamps
var offices = new TreeSet<>(List.of(
    location(47.4979, 19.0402),   // Budapest HQ
    location(51.5074, -0.1278),   // London Branch
    location(48.8566, 2.3522)     // Paris Branch
));

// Generate all possible directions (origin → destination pairs)
var allRoutes = Directions.getAllDirections(offices, offices)
    .collect(Collectors.toList());
// Result: 9 directions (3x3 combinations including self-to-self)
// Budapest→Budapest, Budapest→London, Budapest→Paris,
// London→Budapest, London→London, London→Paris,
// Paris→Budapest, Paris→London, Paris→Paris
// Note: Self-to-self routes represent internal routing within same office
// (yes, forms can get lost in the same building—that's bureaucracy)
```

> **Design Note:** `Direction` is deliberately minimal—it represents topology, not geography. Think of it as an edge in a graph for paperwork routing. If you need actual travel distance or time, use `DirectionFeature` instead or Tracks for full spatio-temporal journeys.

---

## DirectionFeature

A `Direction` enriched with routing metadata: `Distance` and `Duration`. Represents real-world travel information obtained from routing APIs or pre-calculated tables.

### Creating DirectionFeatures

```java
import com.fieldcode.tesseract.direction.Directions;
import com.fieldcode.tesseract.distance.Distances;
import java.time.Duration;

// Create direction feature with routing information
var budapestHQ = location(47.4979, 19.0402);
var londonBranch = location(51.5074, -0.1278);

var routeInfo = Directions.feature(
    budapestHQ,                          // Origin
    londonBranch,                        // Destination
    Distances.ofKilometers(1445),        // Distance: 1,445 km
    Duration.ofHours(2).plusMinutes(30)  // Duration: 2.5 hours (flight time)
);
```

**Real-world scenario:**

```java
// Logistics Coordinator fetches route information from external routing API
// Required for form TPO-5500 ("Field Agent Deployment Authorization")
// (e.g., Google Maps, OpenRouteService, or TPO's internal routing service)

var route = Directions.feature(
    origin,
    destination,
    Distances.ofKilometers(distanceFromAPI),
    Duration.ofSeconds(durationSecondsFromAPI)
);
// Cached for 24 hours to reduce API calls (budget constraints, naturally)
```

### DirectionFeature Operations

**Querying:**

```java
var routeInfo = Directions.feature(
    location(47.4979, 19.0402),
    location(51.5074, -0.1278),
    Distances.ofKilometers(1445),
    Duration.ofHours(2).plusMinutes(30)
);

// DirectionFeature extends Direction, so it has origin/destination
var origin = routeInfo.getOrigin();
var destination = routeInfo.getDestination();

// Plus routing metadata
var distance = routeInfo.getDistance();        // 1,445 km
var duration = routeInfo.getDuration();        // 2.5 hours

// Use in deployment calculations (for form TPO-5500 filing)
System.out.println("=== Field Agent Deployment Route ===");
System.out.println("Budapest HQ → London Branch:");
System.out.println("  Distance: " + distance.toKilometers() + " km");
System.out.println("  Duration: " + duration.toHours() + " hours");
System.out.println("  Avg speed: " + 
    (distance.toKilometers() / duration.toHours()) + " km/h");
// Output: Avg speed: 578 km/h (commercial flight speed)
// Logistics Coordinator files this with agent assignment paperwork
```

> **Use Case:** DirectionFeature bridges spatial primitives and external routing APIs. Fetch route data once, cache it, and reuse for travel time calculations, ETA predictions, or resource scheduling. The Temporal Permit Office Logistics Coordinator uses this to estimate when field agents will arrive at branch offices and plan investigation timelines accordingly. Required attachment for form TPO-5500 ("Field Agent Deployment Authorization").

---

## Track

A spatio-temporal journey combining two `Presence` objects (where + when) with a `Distance`. Represents complete movement through space and time: "Agent Martinez traveled from Budapest at 9:00 to London at 12:00, covering 1,445 km."

### Creating Tracks

```java
import com.fieldcode.tesseract.track.Tracks;

// Method 1: From two presences + distance (for TPO-3001 audit trail)
var departure = presence(moment(9), location(47.4979, 19.0402));   // Budapest HQ at 9:00
var arrival = presence(moment(12), location(51.5074, -0.1278));    // London Branch at 12:00

var journey = Tracks.track(
    departure,
    arrival,
    Distances.ofKilometers(1445)
);
// Required for form TPO-3001 ("Field Agent Movement Log")
// Temporal Compliance Officer Jenkins verifies all agent movements

// Method 2: From DirectionFeature + start time (Logistics Coordinator preferred method)
var routeInfo = Directions.feature(
    location(47.4979, 19.0402),
    location(51.5074, -0.1278),
    Distances.ofKilometers(1445),
    Duration.ofHours(3)
);

var trip = Tracks.track(routeInfo, OffsetDateTime.now());
// Creates track starting now, ending at now + 3 hours
// Logistics Coordinator uses this for real-time agent deployment

// Method 3: From locations + DirectionFeature + start time (most flexible)
var trip2 = Tracks.track(
    location(47.4979, 19.0402),    // Origin: Budapest HQ
    location(51.5074, -0.1278),    // Destination: London Branch
    routeInfo,                      // DirectionFeature (from routing API cache)
    OffsetDateTime.now()            // Departure timestamp
);
// All three methods produce identical tracks—choose based on available data
```

### Track Operations

**Querying:**

```java
var journey = Tracks.track(
    presence(moment(9), location(47.4979, 19.0402)),
    presence(moment(12), location(51.5074, -0.1278)),
    Distances.ofKilometers(1445)
);

// Access presence endpoints
var origin = journey.getOrigin();           // presence(moment(9), Budapest)
var destination = journey.getDestination(); // presence(moment(12), London)

// Access temporal components
var start = journey.getStart();             // moment(9)
var end = journey.getEnd();                 // moment(12)
var duration = journey.getDuration();       // Duration.ofHours(3)

// Access spatial component
var distance = journey.getDistance();       // 1,445 km

// Get temporal interval
var timespan = journey.getInterval();       // interval(9, 12) - the journey's duration

// Get spatial bounds
var locations = journey.getLocationBounds(); // Stream of [origin, destination] presences
```

**Track Connectivity:**

```java
// Verify multi-leg journey continuity (required for form TPO-5500 approval)
var leg1 = Tracks.track(
    presence(moment(9), location(47.4979, 19.0402)),   // Budapest HQ 9:00
    presence(moment(12), location(51.5074, -0.1278)),  // London Branch 12:00
    Distances.ofKilometers(1445)
);

var leg2 = Tracks.track(
    presence(moment(12), location(51.5074, -0.1278)),  // London Branch 12:00
    presence(moment(15), location(40.7128, -74.0060)), // New York Branch 15:00
    Distances.ofKilometers(5570)
);

var isConnected = leg1.isConnected(leg2);  // true - leg1 ends where leg2 begins
// (same location AND moment - no gaps, no paradoxes)
// Temporal Compliance Officer Jenkins approves connected journeys only
```

**Practical Use:**

```java
// Logistics Coordinator plans multi-leg field agent journey
// Required for form TPO-5500 ("Field Agent Deployment Authorization")
public List<Track> planJourney(List<DirectionFeature> routes, OffsetDateTime departureTime) {
    record State(List<Track> tracks, OffsetDateTime currentTime) {}
    
    return routes.stream()
        .reduce(
            new State(new ArrayList<>(), departureTime),
            (state, route) -> {
                var track = Tracks.track(route, state.currentTime);
                var newTracks = new ArrayList<>(state.tracks);
                newTracks.add(track);
                return new State(newTracks, track.getEnd().getDateTime());
            },
            (s1, s2) -> s1  // Combiner (unused for sequential streams)
        )
        .tracks;
}

/**
 * Validates multi-leg journey continuity for TPO-5500 approval.
 * 
 * Temporal Compliance Officer Jenkins requires that all journey legs connect
 * seamlessly - no gaps, no paradoxes, no stranded field agents.
 * 
 * @param legs the journey legs to validate
 * @return true if journey is continuous, false if gaps detected (requires form TPO-9999)
 */
public boolean isValidJourney(List<Track> legs) {
    return IntStream.range(0, legs.size() - 1)
        .allMatch(i -> {
            var currentLeg = legs.get(i);
            var nextLeg = legs.get(i + 1);
            return currentLeg.isConnected(nextLeg);  // No temporal/spatial gaps allowed
        });
}
```

> **Design Philosophy:** Track unifies spatial and temporal concepts into a single, cohesive journey representation. While `Direction` says "from A to B" and `Presence` says "at location L at time T," `Track` tells the complete story: "traveled from location A at time T1 to location B at time T2, covering distance D." Perfect for route planning, travel logs, and spatio-temporal audit trails required by the Temporal Permit Office.

---

## Practical Example

**Field Agent Journey Planner**

Logistics Coordinator Chen plans a multi-city field investigation for Agent Martinez. Filing form TPO-5500 ("Field Agent Deployment Authorization") requires complete route calculations, travel times, and validated journey continuity. Even time travelers can't teleport (yet—form TPO-PORTAL-001 for teleportation permit is still pending review since 2018).

```java
// Field Agent Martinez deployment from Budapest to London
var budapestHQ = location(47.4979, 19.0402);
var londonBranch = location(51.5074, -0.1278);

// Create DirectionFeature with routing information (from external API)
var routeInfo = Directions.feature(
    budapestHQ,
    londonBranch,
    Distances.ofKilometers(1445),
    Duration.ofHours(2).plusMinutes(30)
);

// Create journey track departing at 9:00 UTC today
var journey = Tracks.track(routeInfo, at(9));

// Verify journey details using AssertJ
assertThat(journey.getStart()).isEqualTo(moment(9));                                // Departs 9:00
assertThat(journey.getEnd()).isEqualTo(moment(11).shift(Duration.ofMinutes(30)));   // Arrives 11:30
assertThat(journey.getDistance().toKilometers()).isEqualTo(1445);                   // 1,445 km distance
assertThat(journey.getDuration()).isEqualTo(Duration.ofHours(2).plusMinutes(30));   // 2.5 hours duration

System.out.println("✓ Form TPO-5500 approved - Agent cleared for departure");
```

**What this demonstrates:**

- **Helper notation**: Using `location()`, `at()`, `moment()`, and `interval()` for compact, readable examples
- **DirectionFeature**: Combining locations with routing metadata (distance and duration)
- **Track creation**: Building a spatio-temporal journey from a DirectionFeature and departure time
- **Journey operations**: Verifying start/end moments, distance, and temporal interval

---

**Next Steps:**

- Return to [Temporal Primitives](temporal-primitives.md) to understand time-based operations
- Explore [Interval Collections](interval-collection.md) for combining temporal and spatial queries
- Check out the complete [Documentation Index](fieldcode-tesseract.md) for advanced features

