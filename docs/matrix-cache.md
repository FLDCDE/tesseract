# Direction Matrix Cache

A caching layer in front of expensive direction-matrix computation. Given a set of locations, it decides whether to reuse what's already been fetched, extend it, or give up and fetch everything fresh—so the Temporal Permit Office doesn't pay for the same distance-and-duration lookup twice in one afternoon.

---

## TL;DR

**`DirectionMatrixCacheController`** = The one public entry point (`com.fieldcode.tesseract.matrix.cache`). Wrap your (expensive, probably network-bound) fetch function with `DirectionMatrixCacheController.of(config, fetch)`, then call `.get(locations)` instead of calling your fetch function directly. Everything else in the package is package-private plumbing.

**Two-tier lookup:** An exact-match shortcut (`QuickCache`, hash → hash) is tried first. If the exact same location set was requested before, you get the answer without touching the main store at all. Otherwise, `MatrixStore` searches previously-stored matrices for the cheapest one to reuse or extend.

**Three outcomes per request:** `COVERED` (everything you asked for is already in a stored matrix—just return it), `PARTIAL` (extend a stored matrix with the missing rows/columns and merge), or `FULL` (extending is more expensive than starting over—fetch everything fresh). The cheapest candidate wins.

**Not thread-hostile, but not naive either:** The cache is built to survive concurrent `.get()` calls from multiple scheduling threads—see [Thread Safety](#thread-safety) below for the details.

---

## Table of Contents

- [Foundation](#foundation)
- [How a Request Is Resolved](#how-a-request-is-resolved)
- [Key Types](#key-types)
- [Usage](#usage)
- [Thread Safety](#thread-safety)
- [Next Steps](#next-steps)

---

## Foundation

> **Why this exists:** Direction matrices (distances and durations between every pair of locations in a set) are typically backed by a routing API call, and those aren't free—not in latency, not in your API bill. The Temporal Permit Office's dispatch optimizer asks for direction matrices constantly, often for overlapping or shifting sets of locations as tickets get reassigned. Recomputing from scratch every time is wasteful; this package exists to make "mostly the same request as last time" cheap.
>
> **Package visibility:** Only `DirectionMatrixCacheController` (and the config/statistics types it depends on, in `com.fieldcode.tesseract`) are public. `MatrixStore`, `QuickCache`, `CandidateDirectionMatrix`, `MatrixMerger`, and friends are package-private implementation details. If you find yourself importing them directly, you're probably fighting the API rather than using it.
>
> **You bring the fetch function:** The cache doesn't know how to compute a direction matrix—you supply that as a `Function<DirectionMatrixDimensions, PartialDirectionMatrix>` when constructing the controller. This keeps the cache agnostic to whether you're calling a routing API, a pythagorean approximation, or a stub in a test.

> **Examples notation:** Throughout this document, we use helper methods like `location(lat, lon)` as shorthand. See [Helper Notation](helper-notation.md) for details.

---

## How a Request Is Resolved

`DirectionMatrixCacheController.get(locations)` walks through these steps, in order, stopping as soon as one produces an answer:

1. **Exact match (`QuickCache`)** — hash the requested location set and check whether this *exact* set was resolved before. If so, look up the matching stored matrix by its own hash and return it. No comparison, no merging, no cost calculation.
2. **Candidate search (`MatrixStore.findByRequest`)** — if there's no exact match, every stored matrix is wrapped as a `CandidateDirectionMatrix` and scored:
   - **`COVERED`** — the stored matrix already contains every requested location. Cost is zero; reuse it directly, and register the request's hash in the quick cache so next time skips straight to step 1.
   - **`PARTIAL`** — the stored matrix is missing some locations, but extending it (fetching just the missing rows/columns/corner and merging via `MatrixMerger`) is cheaper than a full fetch.
   - **`FULL`** — extending would cost more than starting over. The candidate is discarded entirely and a fresh fetch is done for the whole request.
   
   The cheapest candidate, by suggestion category then cost, wins.
3. **Nothing stored yet** — with an empty store there's nothing to compare against, so it's a full fetch by definition.

Every freshly fetched or merged matrix is stored back (`MatrixStore.store`) and its hash registered in the quick cache, so it's available as a candidate—or an exact match—for the next request.

`CacheStatistics` (from `.stat()`) tracks each of these paths independently: request count, quick hit/miss, store hit/miss, partial/full fetch counts, and the current cache size—useful for confirming the cache is actually earning its keep in production rather than just adding a layer of mystery.

---

## Key Types

| Type | Role |
|---|---|
| `DirectionMatrixCacheController` | Public entry point. `get(locations)` resolves a request; `stat()` reports cache effectiveness. |
| `MatrixStore` | Holds fetched/merged matrices keyed by hash (`Cache<String, QueryableDirectionMatrix>`), with size/TTL eviction. Finds the best candidate for a request. |
| `QuickCache` | Exact-match shortcut: request hash → stored matrix hash. Skips candidate search entirely on a repeat request. |
| `CandidateDirectionMatrix` | A stored matrix paired with a request, carrying the computed cost and `MatrixFetchSuggestion` (`COVERED`/`PARTIAL`/`FULL`). |
| `MatrixMerger` | Combines a stored matrix with freshly-fetched extension matrices (row, column, corner) into one full matrix. |
| `MultiKeyLongMatrix` | A dense `long[][]` addressed by two arbitrary keys instead of raw indices—the backbone `MatrixMerger` builds the merged distance/duration grids on. |
| `CacheStatistics` | Immutable snapshot of request/hit/miss/fetch/eviction counters. |

---

## Usage

```java
import com.fieldcode.tesseract.DirectionMatrixCacheConfig;
import com.fieldcode.tesseract.matrix.cache.DirectionMatrixCacheController;
import com.fieldcode.tesseract.matrix.DirectionMatrices;
import java.time.Duration;

// Your (expensive) fetch function—swap in a real routing API client here
var fetch = DirectionMatrices::pythagorean;

var config = new DirectionMatrixCacheConfig() {
  public long getStoreCacheMaxSize() { return 5_000; }
  public Duration getStoreCacheExpireAfterAccess() { return Duration.ofHours(1); }
  public long getQuickCacheMaxSize() { return 5_000; }
  public Duration getQuickCacheExpireAfterAccess() { return Duration.ofHours(1); }
};

var cache = DirectionMatrixCacheController.of(config, fetch);

// First call: nothing stored yet, so this fetches everything fresh
var matrix = cache.get(dispatchGroupLocations);

// A later, overlapping request may hit COVERED/PARTIAL and skip the fetch entirely
var updatedMatrix = cache.get(reassignedTicketLocations);

// Check whether the cache is pulling its weight
var stats = cache.stat();
System.out.println("Full fetches avoided: " + stats.getStoreHitCount() + stats.getPartialFetchCount());
```

---

## Thread Safety

The cache is built to be shared across concurrent dispatch/scheduling threads.

- **`MatrixStore` and `QuickCache`** are backed by Guava `Cache`, which is safe for concurrent `get`/`store`/`put` from multiple threads.
- **`Listener`** (the store/evict callback registry) is a plain `ArrayList` with no synchronization. It's safe under the current usage pattern—callbacks are registered once, at construction, before any concurrent access begins—but it is **not** safe for concurrent `register()` calls. Don't register callbacks after the cache is in use.
- **`CacheStatistics` counters** are `AtomicLong`-backed and safe to increment from any thread.

---

## Next Steps

- [Spatial Primitives](spatial-primitives.md) — `Location`, `Distance`, and `Direction`, which direction matrices are built from
- [Helper Notation](helper-notation.md) — shorthand used in the examples above
- Check the complete [Documentation Index](fieldcode-tesseract.md) for the rest of the library
