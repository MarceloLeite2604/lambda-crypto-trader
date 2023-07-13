package com.github.marceloleite2604.cryptotrader.service.pattern.links;

import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import com.github.marceloleite2604.cryptotrader.model.candles.CandleDirection;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlePosition;
import com.github.marceloleite2604.cryptotrader.model.candles.CandleType;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternCheckContext;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternType;
import com.github.marceloleite2604.cryptotrader.service.pattern.TrendAnalyser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class HangingManPatternChecker extends AbstractPatternChecker {

  private static final int MINIMAL_CANDLES_AMOUNT = 5;
  private static final int PATTERN_CANDLES_SIZE = 2;

  private static final List<CandleType> ACCEPTED_CANDLE_TYPES = List.of(
    CandleType.HAMMER,
    CandleType.SHOOTING_STAR,
    CandleType.GRAVESTONE);

  public HangingManPatternChecker(TrendAnalyser trendService) {
    super(PatternType.HANGING_MAN, MINIMAL_CANDLES_AMOUNT, PATTERN_CANDLES_SIZE, trendService);
  }

  @Override
  public Optional<Candle> findMatch(PatternCheckContext patternCheckContext) {

    final var candles = patternCheckContext.getCandles();

    final var firstCandle = candles.get(0);
    final var firstComparison = firstCandle.getComparison();

    if (!CandleDirection.DESCENDING.equals(firstCandle.getDirection())) {
      return Optional.empty();
    }

    if (!CandlePosition.LOWERED.equals(firstComparison.getPosition())) {
      return Optional.empty();
    }

    final var secondCandle = candles.get(1);

    if (!ACCEPTED_CANDLE_TYPES.contains(secondCandle.getType())) {
      return Optional.empty();
    }

    return Optional.of(firstCandle);
  }
}
