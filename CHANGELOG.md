# CHANGELOG

## 1.10.1

* Open source release requirements update

## v1.10.0

* Preparing for open source release
* Added docs folder with documentation markdown files

## v1.8.0

* Location cluster feature added
* DirectionMatrixResolver refactor
* Javadoc improvements
* Removed JUnit 4 dependencies

## v1.7.2

* Tag, TagHolder, TagBuilder improvements

## v1.7.1-rc

* Timeline
* Tag improvements

## v1.7.0

* Added IntervalMapCollection

## v1.6.8

* Added DirectedInterval

## v1.6.7

* Fixed Intervals.days() to handle daylight saving time

## v1.6.6

* Added totalDuration() to IntervalCollection
* Added withZoneIdSameMoment() to Moment
* Added withZoneIdSameInterval() to Interval
* Durations utils for managing duration related operations

## v1.6.5

* Moment compareTo fix in case of using Moments with different time offsets

## v1.6.4

* IntervalCollection Json serializer and deserializer fix in case of using Interval

## v1.6.3

* Added IntervalMap Jackson serializer and deserializer

---

## v1.6.2

* IntervalContainerBuilder extended with enum based id
* Distance now extends Comparable<Distance>

---

## v1.6.1

* IntervalCollection Jackson deserialization fix

--- 

## v1.6.0

* Added Taggable feature
* Added to Distance:
    * boolean lt(other)
    * boolean le(other)
    * boolean gt(other)
    * boolean ge(other)
    * boolean eq(other)
    * boolean ne(other)
* Added getDuration() to Interval
* Added to IntervalCollection:
    * Interval span()
    * boolean isAlways()
    * boolean isNever()
* Added to IntervalSet:
    * boolean distinct(other)
    * boolean intersects(other)
    * boolean connects(other)
* New data structure to manage related interval collections: IntervalContainer
* Taggable interface, Tag, TagHolder, TagPredicate (used by Presence and TaggedIntervalCollection).
* **BREAKING CHANGE:** PresenceSet has been rewritten
* New data structure to handle interval related events: IntervalMap
* Jackson data types for new data structures (except IntervalMap)
* New data structure for handling point to point travel: Movement

---

## v1.5.0

* DirectionMatrix
* CacheMatrix
* IntervalContainer
* Rewritten PresenceSet

---

## v1.4.0

* Fluent iterator implementation
* Temporal boolean logic for IntervalCollection implementation

---

## v1.3.0

* IntervalSet implementation

---

## v1.2.0

Spatial data types implementation:

* Location
* Presence
* Distance
* Direction
* DirectionFeature
* Track
* DirectionMatrix

---

## v1.1.0

Generic Signal and SignalMap implementation:

* Signal
* SignalMap
* NavigableSignal
* Iterator implementations (SignalMap, NavigableSignal)

---

## v1.0.1

Initial version of the library:

* Moment
* Interval
* Jackson datatype for Moment and Interval

---
