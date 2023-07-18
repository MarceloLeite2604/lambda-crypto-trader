package com.github.marceloleite2604.cryptotrader.service.candle;

import com.github.marceloleite2604.cryptotrader.configuration.GeneralConfiguration;
import com.github.marceloleite2604.cryptotrader.model.OffsetDateTimeRange;
import com.github.marceloleite2604.cryptotrader.model.candles.*;
import com.github.marceloleite2604.cryptotrader.model.trades.Trade;
import com.github.marceloleite2604.cryptotrader.model.trades.TradesRequest;
import com.github.marceloleite2604.cryptotrader.service.mercadobitcoin.MercadoBitcoinService;
import com.github.marceloleite2604.cryptotrader.util.DateTimeUtil;
import com.github.marceloleite2604.cryptotrader.util.ValidationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class CandleService {

  public static final BigDecimal TWO = BigDecimal.valueOf(2);

  private final MercadoBitcoinService mercadoBitcoinService;

  private final ValidationUtil validationUtil;

  private final DateTimeUtil dateTimeUtil;

  private final CandleComparisonService candleComparisonService;

  public List<Candle> retrieveCandles(CandlesRequest candlesRequest) {
    validationUtil.throwIllegalArgumentExceptionIfNotValid(candlesRequest, "Invalid request while retrieving candles.");

    if (candlesRequest.getResolution()
      .isRetrievableFromMb()) {
      return retrieveCandlesFromMb(candlesRequest);
    }

    if (candlesRequest.getToTime() == null) {
      throw new IllegalArgumentException("Project is not ready to work with countback for custom candles.");
    }

    return elaborateCandles(candlesRequest);
  }

  private List<Candle> retrieveCandlesFromMb(CandlesRequest candlesRequest) {
    var candles = mercadoBitcoinService.retrieveCandles(candlesRequest);
    candles = analyse(candles, candlesRequest.getActive()
      .getSymbol(), candlesRequest.getResolution());
    return candleComparisonService.compare(candles);
  }

  private List<Candle> elaborateCandles(CandlesRequest candlesRequest) {
    final var trades = retrieveTrades(candlesRequest);

    var candles = createCandles(trades, candlesRequest);
    candles = analyse(candles, candlesRequest.getActive()
      .getSymbol(), candlesRequest.getResolution());
    candles = analyse(candles, candlesRequest.getActive()
      .getSymbol(), candlesRequest.getResolution());

    return candleComparisonService.compare(candles);
  }

  private List<Candle> analyse(List<Candle> candles, String symbol, CandlePrecision precision) {
    return candles.stream()
      .map(candle -> analyse(candle, symbol, precision))
      .collect(Collectors.toCollection(ArrayList::new));
  }

  private Candle analyse(Candle candle, String symbol, CandlePrecision precision) {

    final var size = candle.getHigh()
      .subtract(candle.getLow());

    final var average = candle.getLow()
      .add(size.divide(TWO, GeneralConfiguration.DEFAULT_ROUNDING_MODE));

    BigDecimal bodyTop;
    BigDecimal bodyBottom;
    CandleDirection direction;
    if (candle.getOpen()
      .compareTo(candle.getClose()) > 0) {
      bodyTop = candle.getOpen();
      bodyBottom = candle.getClose();
      direction = CandleDirection.DESCENDING;
    } else if (candle.getOpen()
      .compareTo(candle.getClose()) < 0) {
      bodyTop = candle.getClose();
      bodyBottom = candle.getOpen();
      direction = CandleDirection.ASCENDING;
    } else {
      bodyTop = bodyBottom = candle.getOpen();
      direction = CandleDirection.STALLED;
    }

    final var upperWickSize = candle.getHigh()
      .subtract(bodyTop);

    final var lowerWickSize = bodyBottom.subtract(candle.getLow());

    final var bodySize = bodyTop.subtract(bodyBottom);

    final var bodyAverage = bodyBottom.add(bodySize.divide(TWO, GeneralConfiguration.DEFAULT_ROUNDING_MODE));

    final var upperWickPercentage = size.compareTo(BigDecimal.ZERO) == 0 ?
      BigDecimal.ZERO : upperWickSize.divide(size, GeneralConfiguration.DEFAULT_ROUNDING_MODE);

    final var lowerWickPercentage = size.compareTo(BigDecimal.ZERO) == 0 ?
      BigDecimal.ZERO : lowerWickSize.divide(size, GeneralConfiguration.DEFAULT_ROUNDING_MODE);

    final var bodyPercentage = size.compareTo(BigDecimal.ZERO) == 0 ?
      BigDecimal.ZERO : bodySize.divide(size, GeneralConfiguration.DEFAULT_ROUNDING_MODE);

    final CandleType type = retrieveType(direction, upperWickPercentage, lowerWickPercentage, bodyPercentage);

    return Candle.builder()
      .close(candle.getClose())
      .high(candle.getHigh())
      .low(candle.getLow())
      .open(candle.getOpen())
      .precision(precision)
      .symbol(symbol)
      .timestamp(candle.getTimestamp())
      .volume(candle.getVolume())
      .size(size)
      .bodySize(bodySize)
      .upperWickSize(upperWickSize)
      .lowerWickSize(lowerWickSize)
      .upperWickPercentage(upperWickPercentage)
      .lowerWickPercentage(lowerWickPercentage)
      .bodyPercentage(bodyPercentage)
      .direction(direction)
      .type(type)
      .average(average)
      .bodyAverage(bodyAverage)
      .build();
  }

  private CandleType retrieveType(
    CandleDirection direction,
    BigDecimal upperWickPercentage,
    BigDecimal lowerWickPercentage,
    BigDecimal bodyPercentage) {
    final boolean bodyPresent = bodyPercentage.compareTo(GeneralConfiguration.COMPARISON_THRESHOLD) > 0;

    final boolean upperWickPresent = upperWickPercentage.compareTo(GeneralConfiguration.COMPARISON_THRESHOLD) > 0;

    final boolean lowerWickPresent = lowerWickPercentage.compareTo(GeneralConfiguration.COMPARISON_THRESHOLD) > 0;

    final boolean ascending = CandleDirection.ASCENDING.equals(direction);

    return CandleType.find(upperWickPresent, lowerWickPresent, bodyPresent, ascending);
  }

  private List<Candle> createCandles(List<Trade> trades, CandlesRequest candlesRequest) {
    Collections.sort(trades);

    final var requestRange = OffsetDateTimeRange.builder()
      .start(candlesRequest.getFrom())
      .end(candlesRequest.getToTime())
      .build();

    final var candleRanges = dateTimeUtil.splitRange(requestRange, candlesRequest.getResolution()
      .getDuration());

    final var tradesByRange = trades.stream()
      .collect(Collectors.groupingBy(trade -> {
        final var optionalRange = candleRanges.stream()
          .filter(range -> range.isBetween(trade.getDate()))
          .findFirst();

        if (optionalRange.isEmpty()) {
          log.warn("Trade done at {} is outside proposed range.", trade.getDate());
          final var firstCandle = candleRanges.get(0);
          final var lastCandle = candleRanges.get(candleRanges.size() - 1);

          log.warn("Candles are between {} and {} (exclusive).", firstCandle.getStart(), lastCandle.getEnd());
          throw new IllegalStateException("Trade is not between any valid date time range.");
        }

        return optionalRange.get();
      }));

    candleRanges.forEach(candleRange -> {
      if (!tradesByRange.containsKey(candleRange)) {
        tradesByRange.put(candleRange, Collections.emptyList());
      }
    });

    final var optionalCandlesByTimeRange = tradesByRange.entrySet()
      .stream()
      .map(entry -> Map.entry(entry.getKey(), createCandle(entry.getValue(), entry.getKey(), candlesRequest)))
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    final var candlesByTimeRange = addEmptyCandles(optionalCandlesByTimeRange);

    return candlesByTimeRange.values()
      .stream()
      .sorted(Comparator.comparing(Candle::getTimestamp))
      .collect(Collectors.toList());
  }

  private Map<OffsetDateTimeRange, Candle> addEmptyCandles(Map<OffsetDateTimeRange, Optional<Candle>> candlesByTimeRange) {
    final var sortedEntries = candlesByTimeRange.entrySet()
      .stream()
      .sorted(Comparator.comparing(e -> e.getKey()
        .getStart()))
      .collect(Collectors.toList());

    Map<OffsetDateTimeRange, Candle> result = new HashMap<>();
    Candle previousCandle = null;
    for (Map.Entry<OffsetDateTimeRange, Optional<Candle>> entry : sortedEntries) {
      Candle candle;
      if (entry.getValue()
        .isEmpty()) {
        final var timestamp = entry.getKey()
          .getStart();
        if (previousCandle == null) {
          log.warn("Not enough trades data to elaborate candle for {} time. Skipping it.", timestamp);
          continue;
        }
        candle = Candle.builder()
          .open(previousCandle.getClose())
          .close(previousCandle.getClose())
          .high(previousCandle.getClose())
          .low(previousCandle.getClose())
          .precision(previousCandle.getPrecision())
          .timestamp(timestamp)
          .symbol(previousCandle.getSymbol())
          .volume(BigDecimal.ZERO)
          .build();
      } else {
        candle = entry.getValue()
          .orElseThrow(() -> new IllegalStateException("Candle is empty"));
      }

      result.put(entry.getKey(), candle);
      previousCandle = candle;
    }

    return result;
  }

  private Optional<Candle> createCandle(List<Trade> trades, OffsetDateTimeRange range, CandlesRequest candlesRequest) {

    if (CollectionUtils.isEmpty(trades)) {
      return Optional.empty();
    }

    final var high = trades.stream()
      .map(Trade::getPrice)
      .max(BigDecimal::compareTo)
      .orElseThrow(() -> new IllegalStateException("Could not find high price for candles."));

    final var low = trades.stream()
      .map(Trade::getPrice)
      .min(BigDecimal::compareTo)
      .orElseThrow(() -> new IllegalStateException("Could not find low price for candles."));

    final var open = trades.stream()
      .min(Comparator.comparing(Trade::getDate))
      .map(Trade::getPrice)
      .orElseThrow(() -> new IllegalStateException("Could not find open price for candles."));

    final var close = trades.stream()
      .max(Comparator.comparing(Trade::getDate))
      .map(Trade::getPrice)
      .orElseThrow(() -> new IllegalStateException("Could not find open price for candles."));

    final var volume = trades.stream()
      .map(Trade::getAmount)
      .reduce(BigDecimal.ZERO, BigDecimal::add);

    final var symbol = candlesRequest.getActive()
      .getSymbol();

    final var candle = Candle.builder()
      .high(high)
      .low(low)
      .open(open)
      .close(close)
      .precision(candlesRequest.getResolution())
      .timestamp(range.getStart())
      .symbol(symbol)
      .volume(volume)
      .build();

    return Optional.of(candle);
  }

  private List<Trade> retrieveTrades(CandlesRequest candlesRequest) {
    final var tradesRequest = TradesRequest.builder()
      .from(candlesRequest.getFrom())
      .to(candlesRequest.getToTime())
      .symbol(candlesRequest.getActive()
        .getSymbol())
      .build();

    return mercadoBitcoinService.retrieveTrades(tradesRequest);
  }
}
