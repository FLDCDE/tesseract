package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import com.google.common.testing.FakeTicker;

import java.time.Duration;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("MatrixStore")
class MatrixStoreTest extends DirectionMatrixTestSupport {

  @Test
  @DisplayName("should store a matrix and return its hash")
  void store_Simple_Success() {

    // Arrange: an empty store and a 10-location matrix
    var store = emptyMatrixStore();
    var matrix = matrix(10);

    // Act: store the matrix
    var hash = store.store(matrix);

    // Assert: the store now holds one entry, keyed by the expected hash
    assertThat(store.size()).isOne();
    assertThat(hash).isEqualTo("d6ccb1a0fdba5146a8b9396d12401647bdb610d0f3c244b7bdc574b670011aa4");
  }

  @Test
  @DisplayName("should find previously stored matrices by their hash")
  void findByHash_Stored_Success() {

    // Arrange: three matrices of different sizes, stored under their hashes
    var store = emptyMatrixStore();
    var hash1 = store.store(matrix(5));
    var hash2 = store.store(matrix(10));
    var hash3 = store.store(matrix(15));

    // Act: look each one up by its hash
    var m1 = store.findByHash(hash1);
    var m2 = store.findByHash(hash2);
    var m3 = store.findByHash(hash3);

    // Assert: each lookup returns the matrix it was stored with
    assertThat(m1.getMatrix().getLocations()).isEqualTo(matrix(5).getLocations());
    assertThat(m2.getMatrix().getLocations()).isEqualTo(matrix(10).getLocations());
    assertThat(m3.getMatrix().getLocations()).isEqualTo(matrix(15).getLocations());
  }

  @Test
  @DisplayName("should throw when looking up an unknown hash")
  void findByHash_Unknown_Exception() {

    // Arrange: an empty store, with no matrix stored under "unknown-hash"
    var store = emptyMatrixStore();

    // Act & Assert: looking it up throws
    assertThatThrownBy(() -> store.findByHash("unknown-hash"))
        .isInstanceOf(IllegalStateException.class);
  }

  @Test
  @DisplayName("should invoke store and evict callbacks as entries are stored and expire")
  void callbacks_Success() {

    // Arrange: a store on a fake ticker, with store/evict callbacks registered
    var ticker = new FakeTicker()
        .setAutoIncrementStep(Duration.ofMillis(100));
    var store = emptyMatrixStore(ticker);
    var onStore = new ArrayList<QueryableDirectionMatrix>();
    var onEvict = new ArrayList<String>();
    store.register(onStore::add, onEvict::add);

    // Act: store a matrix, advance the ticker past expiry, then store another
    store.store(matrix(5));
    ticker.advance(Duration.ofDays(2));
    store.store(matrix(10));

    // Assert: both stores were reported, and the expired entry was evicted
    var onStoreLocations = onStore.stream().map(QueryableDirectionMatrix::getLocations);

    assertThat(onStoreLocations).containsExactly(
        locations(5),
        locations(10)
    );

    assertThat(onEvict).containsExactly(
        "bbcd1ead7c42af56305e1e64a685700e83fff4783e7a69667c91798851c0b573"
    );
  }

  @Test
  @Timeout(30)
  @DisplayName("should not throw ArrayIndexOutOfBoundsException when the store is mutated concurrently")
  void findByRequest_ConcurrentStore_NoArrayIndexOutOfBounds() throws InterruptedException {

    // Arrange: a store pre-filled to 100 entries, plus a writer thread that keeps
    // adding more and a reader thread that keeps calling findByRequest concurrently
    var store = emptyMatrixStore();

    for (int i = 0; i < 100; i++) {
      store.store(matrix(i, i + 3));
    }

    var request = ImmutableMatrixRequest.of(locations(0, 3));
    var error = new AtomicReference<Throwable>();
    var start = new CountDownLatch(1);

    var writer = new Thread(() -> {
      try {
        start.await();
        for (int i = 100; i < 2000 && error.get() == null; i++) {
          store.store(matrix(i, i + 3));
        }
      } catch (Throwable t) {
        error.compareAndSet(null, t);
      }
    });

    var reader = new Thread(() -> {
      try {
        start.await();
        for (int i = 0; i < 5000 && error.get() == null; i++) {
          store.findByRequest(request);
        }
      } catch (Throwable t) {
        error.compareAndSet(null, t);
      }
    });

    // Act: run both threads concurrently until they finish
    writer.start();
    reader.start();
    start.countDown();
    writer.join();
    reader.join();

    // Assert: neither thread observed an exception
    assertThat(error.get()).isNull();
  }

}
