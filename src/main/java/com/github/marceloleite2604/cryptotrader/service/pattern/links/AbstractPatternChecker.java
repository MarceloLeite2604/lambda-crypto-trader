package com.github.marceloleite2604.cryptotrader.service.pattern.links;

import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternCheckContext;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternMatch;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternType;
import com.github.marceloleite2604.cryptotrader.model.pattern.trend.TrendType;
import com.github.marceloleite2604.cryptotrader.service.pattern.TrendAnalyser;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
public abstract class AbstractPatternChecker implements PatternChecker {

  private static final BigDecimal MINIMAL_TREND_ACCELERATION = BigDecimal.ZERO;


  private final PatternType patternType;

  private final int minimalCandlesAmount;

  private final int patternCandlesSize;

  private final TrendAnalyser trendService;

  @Setter
  private PatternChecker next;

  private boolean isValidTrend(PatternCheckContext patternCheckContext) {

    var trendCandles = patternCheckContext.getCandles()
      .subList(
        patternCandlesSize,
        patternCandlesSize+4);

    final var trend = trendService.analyse(trendCandles);

    if (!patternType.getTrendType()
      .equals(trend.getType())) {
      return false;
    }

    if (TrendType.DOWNTREND.equals(trend.getType())) {
      return trend.getAcceleration()
      .compareTo(MINIMAL_TREND_ACCELERATION.negate()) > 0;
    }

    if (TrendType.UPTREND.equals(trend.getType())) {
      return trend.getAcceleration()
        .compareTo(MINIMAL_TREND_ACCELERATION) >= 0;
    }

    return false;
  }

  @Override
  public PatternCheckContext check(final PatternCheckContext patternCheckContext) {

    final var candles = patternCheckContext.getCandles();
    if (candles.size() >= minimalCandlesAmount && isValidTrend(patternCheckContext)) {
      findMatch(patternCheckContext).ifPresent(candle ->
      {
        final var active = Active.findBySymbol(candle.getSymbol());

        patternCheckContext.addMatch(PatternMatch.builder()
          .candleTime(candle.getTimestamp())
          .type(patternType)
          .candlePrecision(candle.getPrecision())
          .active(active)
          .build());

        if (log.isDebugEnabled()) {
          final var analysedCandles = candles.subList(0, patternCandlesSize);

          log.debug("{} pattern found analysing the following candles: {}",
            patternType.getName(),
            analysedCandles);
        }
      });
    }

    if (next == null) {
      return patternCheckContext;
    }

    return next.check(patternCheckContext);
  }

  public abstract Optional<Candle> findMatch(PatternCheckContext patternCheckContext);
}
