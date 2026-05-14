package com.fieldcode.tesseract.moment;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.function.Function;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.FiniteMoment;
import com.fieldcode.tesseract.InfiniteMoment;
import com.fieldcode.tesseract.MomentMapper;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static org.assertj.core.api.Assertions.assertThat;

class MomentFunctionTest extends TimeTestSupport {

  private static final OffsetDateTime REFERENCE = OffsetDateTime.of(0, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC);
  private static final Duration SHIFT = Duration.ofDays(365).multipliedBy(1000);

  protected static final MomentMapper<OffsetDateTime> VIRTUAL_INF_MAPPER = new MomentMapper<>() {
    @Override
    public OffsetDateTime map(FiniteMoment finite) {
      return finite.getAt();
    }

    @Override
    public OffsetDateTime map(InfiniteMoment infinite) {
      var shift = SHIFT.multipliedBy(infinite.signum());
      return REFERENCE.plus(shift);
    }
  };

  @Test
  void map_All_Success() {

    Function<FiniteMoment, Integer> finiteMapper = f -> 0;
    Function<InfiniteMoment, Integer> infiniteMapper = InfiniteMoment::signum;

    var f = moment().map(finiteMapper, infiniteMapper);
    var ninf = ninf().map(finiteMapper, infiniteMapper);
    var inf = inf().map(finiteMapper, infiniteMapper);

    assertThat(f).isEqualTo(0);
    assertThat(ninf).isEqualTo(-1);
    assertThat(inf).isEqualTo(1);

  }

  @Test
  void map_WithStaticMapper_Success() {
    var ninf = ninf().map(VIRTUAL_INF_MAPPER);
    var finite = moment().map(VIRTUAL_INF_MAPPER);
    var inf = inf().map(VIRTUAL_INF_MAPPER);

    assertThat(ninf).isEqualTo(REFERENCE.minus(SHIFT));
    assertThat(inf).isEqualTo(REFERENCE.plus(SHIFT));
    assertThat(finite).isEqualTo(at(0));
  }

}
