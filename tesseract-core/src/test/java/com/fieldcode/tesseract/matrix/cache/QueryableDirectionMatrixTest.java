package com.fieldcode.tesseract.matrix.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Location;
import com.fieldcode.tesseract.matrix.DirectionMatrices;

import java.util.SortedSet;
import static com.fieldcode.tesseract.matrix.cache.MatrixFetchSuggestion.COVERED;
import static com.fieldcode.tesseract.matrix.cache.MatrixFetchSuggestion.FULL;
import static com.fieldcode.tesseract.matrix.cache.MatrixFetchSuggestion.PARTIAL;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("QueryableDirectionMatrix")
class QueryableDirectionMatrixTest extends DirectionMatrixTestSupport {

  @Test
  @DisplayName("should compute the full/partial fetch cost and suggestion for a request")
  void cost() {

    //@formatter:off
    // Arrange, Act & Assert (table-driven): for a stored 0-9 matrix, check the full
    // fetch cost, partial fetch cost and resulting suggestion against various requests
    assertCosts(locations(0, 10), locations( 0, 10), 100,   0, COVERED );
    assertCosts(locations(0, 10), locations( 0,  5),  25,   0, COVERED );
    assertCosts(locations(0, 10), locations( 0, 15), 225, 125, PARTIAL );
    assertCosts(locations(0, 10), locations( 3, 15), 144, 125, PARTIAL );
    assertCosts(locations(0, 10), locations( 9, 20), 121, 300, FULL    );
    assertCosts(locations(0, 10), locations(10, 20), 100, 300, FULL    );
    //@formatter:on

  }

  @Test
  @DisplayName("should select the cost matching the candidate's suggestion")
  void getCost_MatchesSuggestion_Success() {

    //@formatter:off
    // Arrange, Act & Assert (table-driven): getCost() must return the suggestion-specific
    // cost — 0 for COVERED, the partial cost for PARTIAL, and the full cost for FULL —
    // since MatrixStore.findByRequest sorts candidates on this value.
    assertCost(locations(0, 10), locations( 0, 10),   0, COVERED );
    assertCost(locations(0, 10), locations( 0,  5),   0, COVERED );
    assertCost(locations(0, 10), locations( 0, 15), 125, PARTIAL );
    assertCost(locations(0, 10), locations( 3, 15), 125, PARTIAL );
    assertCost(locations(0, 10), locations( 9, 20), 121, FULL    );
    assertCost(locations(0, 10), locations(10, 20), 100, FULL    );
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

  private void assertCost(SortedSet<Location> original, SortedSet<Location> required, int expectedCost, MatrixFetchSuggestion expectedSuggestion) {
    var o = DirectionMatrices.pythagorean(original);
    var q = QueryableDirectionMatrix.of(o);
    var request = ImmutableMatrixRequest.of(required);

    var candidate = q.candidate(request);

    assertThat(candidate.getSuggestion()).isEqualTo(expectedSuggestion);
    assertThat(candidate.getCost()).isEqualTo(expectedCost);
  }

}
