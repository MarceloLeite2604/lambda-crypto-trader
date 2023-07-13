package com.github.marceloleite2604.cryptotrader.service.pattern;

import com.github.marceloleite2604.cryptotrader.configuration.GeneralConfiguration;
import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import com.github.marceloleite2604.cryptotrader.model.pattern.trend.Trend;
import com.github.marceloleite2604.cryptotrader.model.pattern.trend.TrendType;
import com.github.marceloleite2604.cryptotrader.util.ComparisonUtil;
import com.github.marceloleite2604.cryptotrader.util.StatisticsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TrendAnalyser {

  private final StatisticsUtil statisticsUtil;

  private final ComparisonUtil comparisonUtil;

  public Trend analyse(final List<Candle> candles) {

    if (candles.size() <= 1) {
      return Trend.builder()
        .type(TrendType.UNDEFINED)
        .candles(candles)
        .acceleration(BigDecimal.ZERO)
        .build();
    }

    final var type = checkTrendType(candles);

    final var acceleration = calculateAcceleration(candles);

    return Trend.builder()
      .type(type)
      .candles(candles)
      .acceleration(acceleration)
      .build();
  }

  private BigDecimal calculateAcceleration(List<Candle> candles) {

    final var closeAverage = statisticsUtil.calculateAverage(candles, Candle::getClose);
    final var closeStandardDeviation = statisticsUtil.calculateStandardDeviation(candles, Candle::getClose);

    final var secondHalfSumCloseDiff = candles
      .subList(0, candles.size() / 2)
      .stream()
      .map(trendCandle -> trendCandle.getClose()
        .subtract(closeAverage))
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    return secondHalfSumCloseDiff.divide(closeStandardDeviation, GeneralConfiguration.DEFAULT_ROUNDING_MODE);
  }

  private TrendType checkTrendType(List<Candle> candles) {
    final var firstCandle = candles.get(0);
    final var firstClose = firstCandle.getClose();

    final var closeAverage = statisticsUtil.calculateAverage(candles, Candle::getClose);
    final var closeStandardDeviation = statisticsUtil.calculateStandardDeviation(candles, Candle::getClose);

    final var firstCloseDiff = firstClose.subtract(closeAverage);

    final var comparisonResult = comparisonUtil.compareRatioUsingMargin(firstCloseDiff, closeStandardDeviation);

    return TrendType.findByComparisonResult(comparisonResult);
  }
}
