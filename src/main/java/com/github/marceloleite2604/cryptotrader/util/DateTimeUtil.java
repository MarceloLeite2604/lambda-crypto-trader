package com.github.marceloleite2604.cryptotrader.util;

import com.github.marceloleite2604.cryptotrader.model.OffsetDateTimeRange;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
public class DateTimeUtil {

  public OffsetDateTime convertEpochToUtcOffsetDateTime(long epoch) {
    final var localDateTime = LocalDateTime.ofEpochSecond(epoch, 0, ZoneOffset.UTC);
    return OffsetDateTime.of(localDateTime, ZoneOffset.UTC);
  }

  public OffsetDateTime convertTimestampWithNanosToUtcOffsetDateTime(long timestampWithNanos) {
    long epochSecond = timestampWithNanos / 1_000_000_000L;
    long nanoAdjustment = timestampWithNanos % 1_000_000_000L;
    final var instant = Instant.ofEpochSecond(epochSecond, nanoAdjustment);
    return OffsetDateTime.ofInstant(instant, ZoneOffset.UTC);
  }

  public List<OffsetDateTimeRange> splitRange(OffsetDateTimeRange range, Duration duration) {
    Assert.notNull(range, "Time range cannot be null.");
    Assert.notNull(duration, "Duration cannot be null.");

    List<OffsetDateTimeRange> result = new ArrayList<>();
    if (range.getDuration()
      .compareTo(duration) <= 0) {
      result.add(range);
      return result;
    }

    var start = range.getStart();

    OffsetDateTime end;

    do {
      end = start.plus(duration);

      if (end.compareTo(range.getEnd()) > 0) {
        end = range.getEnd();
      }

      final var partialRange = OffsetDateTimeRange.builder()
        .start(start)
        .end(end)
        .build();

      result.add(partialRange);

      start = start.plus(duration);

    } while (end.compareTo(range.getEnd()) < 0);

    return result;
  }

  public OffsetDateTime truncateTo(OffsetDateTime offsetDateTime, Duration duration) {
    final var seconds = offsetDateTime.toEpochSecond() % duration
      .toSeconds();
    final var subtrahend = Duration.ofSeconds(seconds);
    return offsetDateTime.minus(subtrahend)
      .truncatedTo(ChronoUnit.MINUTES);
  }
}
