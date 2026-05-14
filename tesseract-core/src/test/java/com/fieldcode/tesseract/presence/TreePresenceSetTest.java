package com.fieldcode.tesseract.presence;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Presence;
import com.fieldcode.tesseract.interval.Intervals;

import static com.fieldcode.tesseract.QueryDirection.BACKWARD;
import static com.fieldcode.tesseract.QueryDirection.FORWARD;
import static org.assertj.core.api.Assertions.assertThat;

class TreePresenceSetTest extends PresenceTestSupport {

  private final Presence p0 = presence(0, 1, 0);
  private final Presence p5 = presence(5, 2, 0);
  private final Presence p10 = presence(10, 3, 0);

  @Test
  void put_Floor_Success() {
    var set = PresenceSets.presences();

    set.put(p0);

    assertThat(set.presences())
        .toIterable()
        .containsExactly(p0);
  }

  @Test
  void floor_Before_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.floor(moment(-1)))
        .isNull();
  }

  @Test
  void floor_SameMoment_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.floor(moment(0)))
        .isEqualTo(p0);
  }

  @Test
  void floor_After_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.floor(moment(1)))
        .isEqualTo(p0);
  }

  @Test
  void ceiling_Before_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.ceiling(moment(9)))
        .isEqualTo(p10);
  }

  @Test
  void ceiling_SameMoment_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.ceiling(moment(10)))
        .isEqualTo(p10);
  }

  @Test
  void ceiling_After_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.ceiling(moment(11)))
        .isNull();
  }

  @Test
  void lower_Before_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.lower(moment(-1)))
        .isNull();
  }

  @Test
  void lower_SameMoment_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.lower(moment(0)))
        .isNull();
  }

  @Test
  void lower_After_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.lower(moment(1)))
        .isEqualTo(p0);
  }

  @Test
  void higher_Before_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.higher(moment(9)))
        .isEqualTo(p10);
  }

  @Test
  void higher_SameMoment_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.higher(moment(10)))
        .isNull();
  }

  @Test
  void higher_After_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.higher(moment(11)))
        .isNull();
  }

  @Test
  void presences_Different_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    assertThat(set.presences())
        .toIterable()
        .containsExactly(p0, p5, p10);
  }

  @Test
  void presences_SameMoment_Success() {
    var p1 = presence(0, 1, 0);
    var p2 = presence(0, 2, 0);
    var p3 = presence(0, 3, 0);
    var set = PresenceSets.presences(p1, p2, p3);

    assertThat(set.presences())
        .toIterable()
        .containsExactly(p3);
  }

  @Test
  void movement_Forward_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    var movements = set.movements(interval(2, 8), FORWARD)
        .list();

    assertThat(movements)
        .containsExactly(
            movement(p0.move(moment(2)), p5),
            movement(p5, p10.move(moment(8)))
        );

  }

  @Test
  void movement_Backward_Success() {
    var set = PresenceSets.presences(p0, p5, p10);

    var movements = set.movements(interval(2, 8), BACKWARD)
        .list();

    assertThat(movements)
        .containsExactly(
            movement(p5, p10.move(moment(8))),
            movement(p0.move(moment(2)), p5)
        );

  }

  // FORWARD -----------------------------

  @Test
  void presences_Type1Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalTo(-1), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(ninf()),
            p0.move(moment(-1))
        );
  }

  @Test
  void presences_Type2Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalTo(0), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(ninf()),
            p0
        );
  }

  @Test
  void presences_Type3Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalTo(2), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(ninf()),
            p0,
            p5.move(moment(2))
        );
  }

  @Test
  void presences_Type4Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalTo(10), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(ninf()),
            p0,
            p5,
            p10
        );
  }

  @Test
  void presences_Type5Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(Intervals.always(), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(ninf()),
            p0,
            p5,
            p10,
            presence(inf())
        );
  }

  @Test
  void presences_Type6Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(0, 1), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p0,
            p5.move(moment(1))
        );
  }


  @Test
  void presences_Type7Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(0, 10), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p0,
            p5,
            p10
        );
  }

  @Test
  void presences_Type8Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalFrom(0), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p0,
            p5,
            p10,
            presence(inf())
        );
  }

  @Test
  void presences_Type9Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(1, 8), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p0.move(moment(1)),
            p5,
            p10.move(moment(8))
        );
  }

  @Test
  void presences_Type10Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(1, 10), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p0.move(moment(1)),
            p5,
            p10
        );
  }

  @Test
  void presences_Type11Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(1, 11), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p0.move(moment(1)),
            p5,
            p10,
            presence(inf()).move(moment(11))
        );
  }

  @Test
  void presences_Type12Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(10, 11), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p10,
            presence(inf()).move(moment(11))
        );
  }

  @Test
  void presences_Type13Forward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalFrom(11), FORWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p10.move(moment(11)),
            presence(inf())
        );
  }

  // BACKWARD -----------------------------

  @Test
  void presences_Type1Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalTo(-1), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p0.move(moment(-1)),
            presence(ninf())
        );
  }

  @Test
  void presences_Type2Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalTo(0), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p0,
            presence(ninf())
        );
  }

  @Test
  void presences_Type3Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalTo(2), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p5.move(moment(2)),
            p0,
            presence(ninf())
        );
  }

  @Test
  void presences_Type4Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalTo(10), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p10,
            p5,
            p0,
            presence(ninf())
        );
  }

  @Test
  void presences_Type5Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(Intervals.always(), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(inf()),
            p10,
            p5,
            p0,
            presence(ninf())
        );
  }

  @Test
  void presences_Type6Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(0, 1), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p5.move(moment(1)),
            p0
        );
  }


  @Test
  void presences_Type7Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(0, 10), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p10,
            p5,
            p0
        );
  }

  @Test
  void presences_Type8Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalFrom(0), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(inf()),
            p10,
            p5,
            p0
        );
  }

  @Test
  void presences_Type9Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(1, 8), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p10.move(moment(8)),
            p5,
            p0.move(moment(1))
        );
  }

  @Test
  void presences_Type10Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(1, 10), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            p10,
            p5,
            p0.move(moment(1))
        );
  }

  @Test
  void presences_Type11Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(1, 11), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(inf()).move(moment(11)),
            p10,
            p5,
            p0.move(moment(1))
        );
  }

  @Test
  void presences_Type12Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(interval(10, 11), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(inf()).move(moment(11)),
            p10
        );
  }

  @Test
  void presences_Type13Backward_Success() {

    var set = PresenceSets.presences(p0, p5, p10);

    var result = set.presences(intervalFrom(11), BACKWARD);

    assertThat(result)
        .toIterable()
        .containsExactly(
            presence(inf()),
            p10.move(moment(11))
        );
  }

}
