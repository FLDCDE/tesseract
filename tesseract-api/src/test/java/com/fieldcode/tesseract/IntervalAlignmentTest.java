package com.fieldcode.tesseract;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.IntervalAlignment.IntervalAlignmentVisitor;

import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_AFTER;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_BEFORE;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_ENDS;
import static com.fieldcode.tesseract.IntervalAlignment.ALIGN_STARTS;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalAlignmentTest {

  @Test
  void accept_AllCases_Success() {

    var visitor = new IntervalAlignmentVisitor<Integer>() {

      @Override
      public Integer alignBefore() {
        return 0;
      }

      @Override
      public Integer alignStarts() {
        return 1;
      }

      @Override
      public Integer alignEnds() {
        return 2;
      }

      @Override
      public Integer alignAfter() {
        return 3;
      }
    };

    assertThat(ALIGN_BEFORE.accept(visitor)).isEqualTo(0);
    assertThat(ALIGN_STARTS.accept(visitor)).isEqualTo(1);
    assertThat(ALIGN_ENDS.accept(visitor)).isEqualTo(2);
    assertThat(ALIGN_AFTER.accept(visitor)).isEqualTo(3);

  }

}
