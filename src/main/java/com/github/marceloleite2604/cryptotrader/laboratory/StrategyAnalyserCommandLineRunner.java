package com.github.marceloleite2604.cryptotrader.laboratory;

import com.github.marceloleite2604.cryptotrader.configuration.GeneralConfiguration;
import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.Side;
import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlePrecision;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlesRequest;
import com.github.marceloleite2604.cryptotrader.service.candle.CandleService;
import com.github.marceloleite2604.cryptotrader.service.pattern.PatternService;
import com.github.marceloleite2604.cryptotrader.util.DateTimeUtil;
import com.github.marceloleite2604.cryptotrader.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@RequiredArgsConstructor
//@Component
@Slf4j
public class StrategyAnalyserCommandLineRunner implements CommandLineRunner {

  private static final int CANDLES_WINDOW_SIZE = 12;

  private static final int TIME_WINDOW_SIZE = 365;

  private static final Active ACTIVE = Active.findBySymbol("BTC");

  private static final CandlePrecision PRECISION = CandlePrecision.ONE_DAY;

  private static final Duration DURATION = PRECISION.getDuration();

  private static final BigDecimal BUY_PROFIT_THRESHOLD = BigDecimal.valueOf(0.10);

  private static final BigDecimal FEE_PERCENTAGE = BigDecimal.valueOf(0.0142);

  private final CandleService candleService;

  private final DateTimeUtil dateTimeUtil;

  private final PatternService patternService;

  private final FormatUtil formatUtil;

  @Override
  public void run(String... args) {
    log.info("Executing strategy analyser.");


    var currentSide = Side.BUY;
    var buyingPrice = BigDecimal.ZERO;

    final var finish = dateTimeUtil.truncateTo(
      OffsetDateTime.now(ZoneOffset.UTC),
      DURATION);

    var begin = finish.minus(DURATION.multipliedBy(TIME_WINDOW_SIZE));
    var end = begin.plus(DURATION.multipliedBy(CANDLES_WINDOW_SIZE));

    final var allCandles = retrieveCandles(begin, finish);

    var total = BigDecimal.ONE;

    for (int count = CANDLES_WINDOW_SIZE; count < allCandles.size(); count++) {
      final var candles = allCandles.subList(count - CANDLES_WINDOW_SIZE, count);
      final var time = candles.get(CANDLES_WINDOW_SIZE - 1)
        .getTimestamp();

      final var patternMatches = patternService.check(candles);

      final var finalCurrentSide = currentSide;
      final var optionalCurrentSidePatterMatch = patternMatches.stream()
        .filter(patternMatch -> patternMatch.getType()
          .getSide()
          .equals(finalCurrentSide))
        .findFirst();

      final var closePrice = candles.get(candles.size() - 1)
        .getClose();
      final var profit = buyingPrice.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO :
        closePrice.subtract(buyingPrice)
          .divide(buyingPrice, GeneralConfiguration.DEFAULT_ROUNDING_MODE);

      if (optionalCurrentSidePatterMatch.isPresent()) {
        log.info("{}: {} pattern found.", time, optionalCurrentSidePatterMatch.get()
          .getType()
          .getName());
        if (Side.BUY.equals(currentSide)) {

          buyingPrice = closePrice;
          currentSide = Side.SELL;
          log.info("{}: Buying.", time);
        } else {

          if (profit.compareTo(BUY_PROFIT_THRESHOLD) >= 0) {
            log.info("{}: Selling with profit of {}.", time, formatUtil.toPercentage(profit));
            currentSide = Side.BUY;
            total = total.multiply(BigDecimal.ONE.add(profit.subtract(FEE_PERCENTAGE)));
            log.info("{}: Total profit is {}.", time, formatUtil.toPercentage(total.subtract(BigDecimal.ONE)));
          }
        }
      }

      begin = begin.plus(DURATION);
      end = end.plus(DURATION);
    }
    log.info("Total profit: {}", formatUtil.toPercentage(total.subtract(BigDecimal.ONE)));
  }

  private List<Candle> retrieveCandles(OffsetDateTime begin, OffsetDateTime end) {
    final var candlesRequest = CandlesRequest.builder()
      .resolution(PRECISION)
      .from(begin)
      .toTime(end)
      .active(ACTIVE)
      .build();

    return candleService.retrieveCandles(candlesRequest);
  }
}

