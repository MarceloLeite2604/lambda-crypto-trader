package com.github.marceloleite2604.cryptotrader.model;

import lombok.*;

import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class OffsetDateTimeRange {

  private final OffsetDateTime start;

  private final OffsetDateTime end;

  public boolean isBetween(OffsetDateTime offsetDateTime) {
    return start.compareTo(offsetDateTime) <= 0 &&
      end.compareTo(offsetDateTime) > 0;
  }

  public Duration getDuration() {
    return Duration.between(start, end);
  }

  public boolean equals(final Object o) {
    if (o == this) return true;
    if (!(o instanceof final OffsetDateTimeRange other)) return false;
    if (!other.canEqual(this)) return false;
    final Object thisStart = this.getStart();
    final Object otherStart = other.getStart();
    if (!Objects.equals(thisStart, otherStart)) return false;
    final Object thisEnd = this.getEnd();
    final Object otherEnd = other.getEnd();
    return Objects.equals(thisEnd, otherEnd);
  }

  protected boolean canEqual(final Object other) {
    return other instanceof OffsetDateTimeRange;
  }

  public int hashCode() {
    final int PRIME = 59;
    int result = 1;
    final Object thisStart = this.getStart();
    result = result * PRIME + (thisStart == null ? 43 : thisStart.hashCode());
    final Object thisEnd = this.getEnd();
    result = result * PRIME + (thisEnd == null ? 43 : thisEnd.hashCode());
    return result;
  }
}
