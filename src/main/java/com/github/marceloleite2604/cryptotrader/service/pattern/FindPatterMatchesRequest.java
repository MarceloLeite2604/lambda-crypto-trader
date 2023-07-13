package com.github.marceloleite2604.cryptotrader.service.pattern;

import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlePrecision;
import lombok.*;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class FindPatterMatchesRequest {

  @NotNull
  private final Active active;

  @NotNull
  private final CandlePrecision candlePrecision;

  @NotNull
  private final OffsetDateTime start;

  @NotNull
  private final OffsetDateTime end;

  @AssertTrue(message = "start time must be before or equal end time.")
  private boolean isStartBeforeOrEqualEnd() {
    return start.compareTo(end) <= 0;
  }
}
