package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.Test;

import com.google.common.testing.FakeTicker;

import java.time.Duration;
import java.util.ArrayList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MatrixStoreTest extends DirectionMatrixTestSupport {

  @Test
  void store_Simple_Success() {
    var store = emptyMatrixStore();

    assertThat(store.size()).isZero();

    var matrix = matrix(10);

    var hash = store.store(matrix);
    assertThat(store.size()).isOne();

    assertThat(hash).isEqualTo("d6ccb1a0fdba5146a8b9396d12401647bdb610d0f3c244b7bdc574b670011aa4");
  }

  @Test
  void findByHash_Stored_Success() {

    var store = emptyMatrixStore();

    var hash1 = store.store(matrix(5));
    var hash2 = store.store(matrix(10));
    var hash3 = store.store(matrix(15));

    var m1 = store.findByHash(hash1);
    var m2 = store.findByHash(hash2);
    var m3 = store.findByHash(hash3);

    assertThat(m1.getMatrix().getLocations())
        .isEqualTo(matrix(5).getLocations());

    assertThat(m2.getMatrix().getLocations())
        .isEqualTo(matrix(10).getLocations());

    assertThat(m3.getMatrix().getLocations())
        .isEqualTo(matrix(15).getLocations());
  }

  @Test
  void findByHash_Unknown_Exception() {

    var store = emptyMatrixStore();

    assertThatThrownBy(() -> store.findByHash("unknown-hash"))
        .isInstanceOf(IllegalStateException.class);

  }


  @Test
  void callbacks_Success() {

    var ticker = new FakeTicker()
        .setAutoIncrementStep(Duration.ofMillis(100));

    var store = emptyMatrixStore(ticker);

    var onStore = new ArrayList<QueryableDirectionMatrix>();
    var onEvict = new ArrayList<String>();

    store.register(onStore::add, onEvict::add);

    store.store(matrix(5));

    ticker.advance(Duration.ofDays(2));

    store.store(matrix(10));

    var onStoreLocations = onStore.stream().map(QueryableDirectionMatrix::getLocations);

    assertThat(onStoreLocations).containsExactly(
        locations(5),
        locations(10)
    );

    assertThat(onEvict).containsExactly(
        "bbcd1ead7c42af56305e1e64a685700e83fff4783e7a69667c91798851c0b573"
    );

  }

}
