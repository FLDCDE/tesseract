package com.fieldcode.tesseract.interval;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("BranchlessOperators")
class BranchlessOperatorsTest {

  @ParameterizedTest(name = "negative value {0} produces bit flag 1 (0b001)")
  @ValueSource(ints = {-1, -5, -100, -1000, Integer.MIN_VALUE})
  @DisplayName("negative values encode as 1 (0b001)")
  void testSignFlagNegative(int value) {
    int result = BranchlessOperators.signFlag(value);
    assertThat(result).isEqualTo(1);
  }

  @Test
  @DisplayName("zero encodes as 2 (0b010)")
  void testSignFlagZero() {
    int result = BranchlessOperators.signFlag(0);
    assertThat(result).isEqualTo(2);
  }

  @ParameterizedTest(name = "positive value {0} produces bit flag 4 (0b100)")
  @ValueSource(ints = {1, 5, 100, 1000, Integer.MAX_VALUE})
  @DisplayName("positive values encode as 4 (0b100)")
  void testSignFlagPositive(int value) {
    int result = BranchlessOperators.signFlag(value);
    assertThat(result).isEqualTo(4);
  }

  @ParameterizedTest(name = "signFlag({0}) -> {1} and is one-hot")
  @CsvSource({
      "-1,1",
      "0,2",
      "1,4"
  })
  @DisplayName("all outputs are power-of-two flags")
  void testSignFlagOutputArePowersOfTwo(int value, int expected) {
    int result = BranchlessOperators.signFlag(value);
    assertThat(result)
        .as("signFlag(%d) should be %d", value, expected)
        .isEqualTo(expected);
    assertThat(Integer.bitCount(result)).isEqualTo(1);
  }

  @Test
  @DisplayName("edge cases: boundary values")
  void testSignFlagBoundaryValues() {
    assertThat(BranchlessOperators.signFlag(Integer.MIN_VALUE)).isEqualTo(1);
    assertThat(BranchlessOperators.signFlag(-1)).isEqualTo(1);
    assertThat(BranchlessOperators.signFlag(0)).isEqualTo(2);
    assertThat(BranchlessOperators.signFlag(1)).isEqualTo(4);
    assertThat(BranchlessOperators.signFlag(Integer.MAX_VALUE)).isEqualTo(4);
  }

  @ParameterizedTest(name = "decodeIntervalRelationFlag({0}) -> {1}")
  @CsvSource({
      "33,1",
      "34,2",
      "36,4",
      "40,128",
      "48,256",
      "68,8",
      "72,64",
      "80,512",
      "132,16",
      "136,32",
      "144,1024",
      "272,2048",
      "528,4096"
  })
  @DisplayName("decodeIntervalRelationFlag maps all 13 valid keys")
  void testDecodeIntervalToIntervalRelationFlagKnownMappings(int input, int expected) {
    assertThat(BranchlessOperators.decodeIntervalToIntervalRelationFlag(input)).isEqualTo(expected);
  }

  @ParameterizedTest(name = "decodeIntervalRelationFlag({0}) returns one-hot power-of-two")
  @ValueSource(ints = {33, 34, 36, 40, 48, 68, 72, 80, 132, 136, 144, 272, 528})
  @DisplayName("decodeIntervalRelationFlag outputs one-hot flags for valid keys")
  void testDecodeIntervalToIntervalRelationFlagOutputsPowerOfTwo(int input) {
    int result = BranchlessOperators.decodeIntervalToIntervalRelationFlag(input);
    assertThat(result).isPositive();
    assertThat(Integer.bitCount(result)).isEqualTo(1);
  }

  @ParameterizedTest(name = "decodeMomentRelationFlag({0}) -> {1}")
  @CsvSource({
      "9,1",
      "17,2",
      "33,4",
      "34,8",
      "36,16"
  })
  @DisplayName("decodeMomentRelationFlag maps all 5 valid keys")
  void testDecodeMomentToIntervalRelationFlagKnownMappings(int input, int expected) {
    assertThat(BranchlessOperators.decodeMomentToIntervalRelationFlag(input)).isEqualTo(expected);
  }

  @ParameterizedTest(name = "decodeMomentRelationFlag({0}) returns one-hot power-of-two")
  @ValueSource(ints = {9, 17, 33, 34, 36})
  @DisplayName("decodeMomentRelationFlag outputs one-hot flags for valid keys")
  void testDecodeMomentToIntervalRelationFlagOutputsPowerOfTwo(int input) {
    int result = BranchlessOperators.decodeMomentToIntervalRelationFlag(input);
    assertThat(result).isPositive();
    assertThat(Integer.bitCount(result)).isEqualTo(1);
  }

}
