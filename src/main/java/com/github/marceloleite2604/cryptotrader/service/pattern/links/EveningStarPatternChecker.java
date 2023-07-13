package com.github.marceloleite2604.cryptotrader.service.pattern.links;

import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import com.github.marceloleite2604.cryptotrader.model.candles.CandleDirection;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlePosition;
import com.github.marceloleite2604.cryptotrader.model.candles.CandleProportion;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternCheckContext;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternType;
import com.github.marceloleite2604.cryptotrader.service.pattern.TrendAnalyser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class EveningStarPatternChecker extends AbstractPatternChecker {

  private static final int MINIMAL_CANDLES_AMOUNT = 5;
  private static final int PATTERN_CANDLES_SIZE = 3;

  private static final List<CandleProportion> VALID_CANDLE_SIZE_CATEGORIES = List.of(
    CandleProportion.MEDIUM,
    CandleProportion.MEDIUM_LARGE,
    CandleProportion.LARGE,
    CandleProportion.VERY_LARGE,
    CandleProportion.ENORMOUS);

  public EveningStarPatternChecker(TrendAnalyser trendService) {
    super(PatternType.EVENING_STAR, MINIMAL_CANDLES_AMOUNT, PATTERN_CANDLES_SIZE, trendService);
  }

  @Override
  public Optional<Candle> findMatch(PatternCheckContext patternCheckContext) {

    final var candles = patternCheckContext.getCandles();

    final var thirdCandle = candles.get(2);
    final var thirdComparison = thirdCandle.getComparison();

    if (!VALID_CANDLE_SIZE_CATEGORIES.contains(thirdComparison.getBodyProportion())) {
      return Optional.empty();
    }

    if (!CandleDirection.ASCENDING.equals(thirdCandle.getDirection())) {
      return Optional.empty();
    }

    final var secondCandle = candles.get(1);
    final var secondComparison = secondCandle.getComparison();

    if (thirdComparison.getBodyProportion()
      .ordinal() <= secondComparison.getBodyProportion()
      .ordinal()) {
      return Optional.empty();
    }

    if (!CandlePosition.RAISED.equals(secondComparison.getPosition())) {
      return Optional.empty();
    }

    final var firstCandle = candles.get(0);
    final var firstComparison = firstCandle.getComparison();

    if (firstComparison.getBodyProportion()
      .ordinal() <= secondComparison.getBodyProportion()
      .ordinal()) {
      return Optional.empty();
    }

    if (!CandleDirection.DESCENDING.equals(firstCandle.getDirection())) {
      return Optional.empty();
    }

    if (firstCandle.getOpen()
      .compareTo(secondCandle.getOpen()) >= 0) {
      return Optional.empty();
    }

    if (firstCandle.getClose()
      .compareTo(thirdCandle.getAverage()) > 0) {
      return Optional.empty();
    }

    return Optional.of(firstCandle);
  }
}
