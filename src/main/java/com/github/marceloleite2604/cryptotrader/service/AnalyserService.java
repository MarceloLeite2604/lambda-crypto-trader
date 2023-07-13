package com.github.marceloleite2604.cryptotrader.service;

import com.github.marceloleite2604.cryptotrader.configuration.GeneralConfiguration;
import com.github.marceloleite2604.cryptotrader.model.Action;
import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.OffsetDateTimeRange;
import com.github.marceloleite2604.cryptotrader.model.Side;
import com.github.marceloleite2604.cryptotrader.model.account.Account;
import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlePrecision;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlesRequest;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternMatch;
import com.github.marceloleite2604.cryptotrader.properties.MonitoringProperties;
import com.github.marceloleite2604.cryptotrader.service.candle.CandleService;
import com.github.marceloleite2604.cryptotrader.service.pattern.PatternService;
import com.github.marceloleite2604.cryptotrader.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnalyserService {

  private final MonitoringProperties monitoringProperties;

  private final ActionService actionService;

  private final PatternService patternService;

  private final CandleService candleService;

  private final DateTimeUtil dateTimeUtil;

  public List<Action> analyse(Account account) {

    return monitoringProperties.getActives()
      .stream()
      .map(Active::findByBase)
      .map(active -> analyse(account, active))
      .filter(Optional::isPresent)
      .map(Optional::get)
      .toList();
  }

  private Optional<Action> analyse(Account account, Active active) {
    final var optionalLastOrder = account.retrieveLastOrderFor(active);

    final var candles = retrieveCandles(active);

    final var patternMatches = patternService.check(candles);

    if (optionalLastOrder.isEmpty() ||
      Side.SELL.name()
        .equalsIgnoreCase(optionalLastOrder.get()
          .getSide())) {
      return analyseBuyStrategy(active, patternMatches);
    } else {
      return analyseSellStrategy(account, active, patternMatches, candles);
    }
  }

  private Optional<Action> analyseBuyStrategy(Active active, List<PatternMatch> patternMatches) {

    final var buyPatternMatches = patternService.findPatternMatchesBySide(patternMatches, Side.BUY);

    if (CollectionUtils.isNotEmpty(buyPatternMatches)) {

      final Action action = actionService.create(active, buyPatternMatches);

      return Optional.of(action);
    }

    return Optional.empty();
  }

  private Optional<Action> analyseSellStrategy(
    Account account,
    Active active,
    List<PatternMatch> patternMatches,
    List<Candle> candles) {

    final var sellPatternMatches = patternService.findPatternMatchesBySide(patternMatches, Side.SELL);

    if (CollectionUtils.isNotEmpty(sellPatternMatches)) {
      final BigDecimal profit = calculateProfit(account, active, candles);

      if (profit.compareTo(monitoringProperties.getProfitThreshold()) >= 0) {
        final Action action = actionService.create(active, sellPatternMatches, profit);

        return Optional.of(action);
      }
    }

    return Optional.empty();
  }

  private BigDecimal calculateProfit(Account account, Active active, List<Candle> candles) {
    final var optionalLastBuyingOrder = account.retrieveLastSideOrder(active, Side.BUY);

    if (optionalLastBuyingOrder.isEmpty()) {
      final var message = String.format("Could not find last buying order for \"%s\" active.", active);
      throw new IllegalStateException(message);
    }

    final var lastBuyingOrder = optionalLastBuyingOrder.get();
    final var buyingPrice = lastBuyingOrder.getAveragePrice();
    final var lastClose = candles.get(0)
      .getClose();

    return buyingPrice.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO :
      lastClose.subtract(buyingPrice)
        .divide(buyingPrice, GeneralConfiguration.DEFAULT_ROUNDING_MODE);
  }

  private List<Candle> retrieveCandles(Active active) {

    final var precision = monitoringProperties.getPrecision();
    final var quantity = monitoringProperties.getQuantity();

    final var end = dateTimeUtil.truncateTo(
      OffsetDateTime.now(ZoneOffset.UTC),
      precision.getDuration());

    var start = end
      .minus(precision.getDuration()
        .multipliedBy(quantity));

    final var range = OffsetDateTimeRange.builder()
      .start(start)
      .end(end)
      .build();

    final var candlesRequest = createCandlesRequest(range, precision, active);
    log.debug("Retrieving candles: {}", candlesRequest);
    final var candles = candleService.retrieveCandles(candlesRequest);
    candles.sort(Comparator.reverseOrder());
    return candles;
  }

  private CandlesRequest createCandlesRequest(OffsetDateTimeRange range, CandlePrecision resolution, Active active) {
    return CandlesRequest.builder()
      .active(active)
      .resolution(resolution)
      .toTime(range.getEnd())
      .from(range.getStart())
      .build();
  }
}
