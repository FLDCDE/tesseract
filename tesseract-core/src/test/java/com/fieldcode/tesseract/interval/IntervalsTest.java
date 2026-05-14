package com.fieldcode.tesseract.interval;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import com.fieldcode.tesseract.Interval;
import com.fieldcode.tesseract.Moment;
import com.fieldcode.tesseract.test_utils.TimeTestSupport;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

class IntervalsTest extends TimeTestSupport {

  private static final DateTimeFormatter FORMAT = DateTimeFormatter.ofPattern("Hmm");

  @Test
  void step() {

    var interval = interval(0, 5);

    var ticks = Intervals.step(interval, Duration.ofMinutes(30))
        .map(FORMAT::format);

    assertThat(ticks)
        .toIterable()
        .containsExactly("000", "030", "100", "130", "200", "230", "300", "330", "400", "430");
  }

  @Test
  void days_Truncated_Success() {

    var interval = interval(12, 6 + 3 * 24);

    var bounds = Intervals.days(interval, UTC, true)
        .flatMap(Interval::getBounds)
        .map(Moment::getAt)
        .stream()
        .sorted()
        .distinct();

    assertThat(bounds)
        .containsExactly(
            at(12),
            at(24),
            at(48),
            at(72),
            at(78)
        );
  }

  @Test
  void day_DaylightSaving_Success() {

    var at = OffsetDateTime.of(2024, 3, 30, 12, 0, 0, 0, UTC);

    var day = Intervals.day(at, ZoneId.of("Europe/Budapest"));

    var expectedStart = OffsetDateTime.of(2024, 3, 30, 0, 0, 0, 0, ZoneOffset.of("+01:00"));
    var expectedEnd = OffsetDateTime.of(2024, 3, 31, 0, 0, 0, 0, ZoneOffset.of("+02:00"));

    var expected = Intervals.interval(expectedStart, expectedEnd);
    assertThat(day)
        .isEqualTo(expected);
  }

  @Test
  void days_FullDays_Success() {

    var interval = interval(12, 6 + 3 * 24);

    var bounds = Intervals.days(interval, UTC, false)
        .flatMap(Interval::getBounds)
        .map(Moment::getAt)
        .stream()
        .sorted()
        .distinct();

    assertThat(bounds)
        .containsExactly(
            at(0),
            at(24),
            at(48),
            at(72),
            at(96)
        );
  }


  @Test
  void days_America_Success() {

    var window = Intervals.interval(
        OffsetDateTime.parse("2024-01-25T05:00:00.000Z"),
        OffsetDateTime.parse("2024-01-26T05:00:00.000Z")
    );

    var from = OffsetTime.parse("00:00:00Z");
    var to = OffsetTime.parse("23:59:00Z");

    var openingHours = Intervals.daily(window, from.toLocalTime(), to.toLocalTime(), ZoneId.of("America/New_York"), true).list();

    var expectedOpeningHours = Intervals.interval(
        OffsetDateTime.parse("2024-01-25T05:00:00.000Z"),
        OffsetDateTime.parse("2024-01-26T04:59:00.000Z")
    );

    assertThat(openingHours)
        .hasSize(1)
        .containsExactly(expectedOpeningHours);
  }

  @Test
  void containsLeapDayInterval_2024_Success() {
    var from = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, UTC)
        .atZoneSameInstant(ZoneId.of("Europe/Tirane")).toOffsetDateTime();
    var to = from.plusYears(1);

    var days = Intervals.days(Intervals.interval(from, to), ZoneId.of("Europe/Tirane"), false).stream().collect(Collectors.toList());

    assertThat(days).contains(
        Intervals.interval(OffsetDateTime.parse("2024-02-28T00:00+01:00"), OffsetDateTime.parse("2024-02-29T00:00+01:00")),
        Intervals.interval(OffsetDateTime.parse("2024-02-29T00:00+01:00"), OffsetDateTime.parse("2024-03-01T00:00+01:00"))
    );
  }

  @Test
  void containsDLSIntervals_2023_Success() {
    var from = OffsetDateTime.of(2023, 1, 1, 0, 0, 0, 0, UTC)
        .atZoneSameInstant(ZoneId.of("Europe/Tirane")).toOffsetDateTime();
    var to = from.plusYears(1);

    var days = Intervals.days(Intervals.interval(from, to), ZoneId.of("Europe/Tirane"), false).stream().collect(Collectors.toList());

    assertThat(days).contains(
        Intervals.interval(OffsetDateTime.parse("2023-03-26T00:00+01:00"), OffsetDateTime.parse("2023-03-27T00:00+02:00")),
        Intervals.interval(OffsetDateTime.parse("2023-10-29T00:00+02:00"), OffsetDateTime.parse("2023-10-30T00:00+01:00"))
    );
  }

  @Test
  void containsDLSIntervals_2024_Success() {
    var from = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, UTC)
        .atZoneSameInstant(ZoneId.of("Europe/Tirane")).toOffsetDateTime();
    var to = from.plusYears(1);

    var days = Intervals.days(Intervals.interval(from, to), ZoneId.of("Europe/Tirane"), true).stream().collect(Collectors.toList());

    assertThat(days).contains(
        Intervals.interval(OffsetDateTime.parse("2024-03-31T00:00+01:00"), OffsetDateTime.parse("2024-04-01T00:00+02:00")),
        Intervals.interval(OffsetDateTime.parse("2024-10-27T00:00+02:00"), OffsetDateTime.parse("2024-10-28T00:00+01:00"))
    );
  }

  @Test
  void groupDaysByHours_2023_Success() {
    var from = OffsetDateTime.of(2023, 1, 1, 0, 0, 0, 0, UTC)
        .atZoneSameInstant(ZoneId.of("Europe/Tirane")).toOffsetDateTime();
    var to = from.plusYears(1).minusDays(1);

    var days = Intervals.days(Intervals.interval(from, to), ZoneId.of("Europe/Tirane"), false);

    var groupedByHours = days.map(day -> day.getDuration().get())
        .stream()
        .collect(Collectors.groupingBy(
            Function.identity(),
            Collectors.counting()
        ));

    assertThat(groupedByHours).containsExactlyInAnyOrderEntriesOf(
        Map.of(
            Duration.ofHours(23), 1L,
            Duration.ofHours(25), 1L,
            Duration.ofHours(24), 363L
        )
    );
  }

  @Test
  void groupDaysByHoursLeapYear_2024_Success() {
    var from = OffsetDateTime.of(2024, 1, 1, 0, 0, 0, 0, UTC)
        .atZoneSameInstant(ZoneId.of("Europe/Tirane")).toOffsetDateTime();
    var to = from.plusYears(1).minusDays(1);

    var days = Intervals.days(Intervals.interval(from, to), ZoneId.of("Europe/Tirane"), false);

    var groupedByHours = days
        .map(day -> day.getDuration().get())
        .stream()
        .collect(Collectors.groupingBy(
            Function.identity(),
            Collectors.counting()
        ));

    assertThat(groupedByHours).containsExactlyInAnyOrderEntriesOf(
        Map.of(
            Duration.ofHours(23), 1L,
            Duration.ofHours(25), 1L,
            Duration.ofHours(24), 364L
        )
    );
  }

}
