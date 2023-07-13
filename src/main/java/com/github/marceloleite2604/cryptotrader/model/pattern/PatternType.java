package com.github.marceloleite2604.cryptotrader.model.pattern;

import com.github.marceloleite2604.cryptotrader.model.Side;
import com.github.marceloleite2604.cryptotrader.model.pattern.trend.TrendType;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum PatternType {
  HAMMER("Hammer", Side.BUY, TrendType.DOWNTREND),
  INVERSE_HAMMER("Inverse Hammer", Side.BUY, TrendType.DOWNTREND),
  DARK_CLOUD_COVER("Dark Cloud Cover", Side.SELL, TrendType.UPTREND),
  PIERCING_LINE("Piercing Line", Side.BUY, TrendType.DOWNTREND),
  BULLISH_ENGULFING("Bullish Engulfing", Side.BUY, TrendType.DOWNTREND),
  BEARISH_ENGULFING("Bearish Engulfing", Side.SELL, TrendType.UPTREND),
  MORNING_STAR("Morning Star", Side.BUY, TrendType.DOWNTREND),
  EVENING_STAR("Evening Star", Side.SELL, TrendType.UPTREND),
  HANGING_MAN("Hanging Man", Side.SELL, TrendType.UPTREND);

  private final String name;

  private final Side side;

  private final TrendType trendType;
}
