package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.matrix.DirectionMatrices;

import java.util.SortedSet;
import static com.fieldcode.tesseract.matrix.cache.MatrixFetchSuggestion.COVERED;
import static com.fieldcode.tesseract.matrix.cache.MatrixFetchSuggestion.FULL;
import static com.fieldcode.tesseract.matrix.cache.MatrixFetchSuggestion.PARTIAL;
import static org.assertj.core.api.Assertions.assertThat;

class QueryableDirectionMatrixTest extends DirectionMatrixTestSupport {

  @Test
  void cost() {

    //@formatter:off
    assertCosts(locations(0, 10), locations( 0, 10), 100,   0, COVERED );
    assertCosts(locations(0, 10), locations( 0,  5),  25,   0, COVERED );
    assertCosts(locations(0, 10), locations( 0, 15), 225, 125, PARTIAL );
    assertCosts(locations(0, 10), locations( 3, 15), 144, 125, PARTIAL );
    assertCosts(locations(0, 10), locations( 9, 20), 121, 300, FULL    );
    assertCosts(locations(0, 10), locations(10, 20), 100, 300, FULL    );
    //@formatter:on

  }

  private void assertCosts(SortedSet<Location> original, SortedSet<Location> required, int expectedFullCost, int expectedPartialCost, MatrixFetchSuggestion expectedSuggestion) {
    var o = DirectionMatrices.pythagorean(original);
    var q = QueryableDirectionMatrix.of(o);
    var request = ImmutableMatrixRequest.of(required);

    var candidate = q.candidate(request);

    assertThat(candidate.getFullFetchCost()).isEqualTo(expectedFullCost);
    assertThat(candidate.getPartialFetchCost()).isEqualTo(expectedPartialCost);
    assertThat(candidate.getSuggestion()).isEqualTo(expectedSuggestion);
  }

}
