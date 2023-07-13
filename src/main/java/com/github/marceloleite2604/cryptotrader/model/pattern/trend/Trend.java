package com.github.marceloleite2604.cryptotrader.model.pattern.trend;

import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@ToString
public class Trend {

  private final List<Candle> candles;

  private final TrendType type;

  private final BigDecimal acceleration;
}
