package com.github.marceloleite2604.cryptotrader.service.candle;

import com.github.marceloleite2604.cryptotrader.configuration.GeneralConfiguration;
import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import com.github.marceloleite2604.cryptotrader.model.candles.CandleComparison;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlePosition;
import com.github.marceloleite2604.cryptotrader.model.candles.CandleProportion;
import com.github.marceloleite2604.cryptotrader.util.ComparisonUtil;
import com.github.marceloleite2604.cryptotrader.util.StatisticsUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

@Component
@Slf4j
@RequiredArgsConstructor
public class CandleComparisonService {

  private final StatisticsUtil statisticsUtil;

  private final ComparisonUtil comparisonUtil;

  public List<Candle> compare(List<Candle> candles) {

    if (CollectionUtils.isEmpty(candles)) {
      return Collections.emptyList();
    }

    Collections.sort(candles);

    final List<Candle> result = new ArrayList<>(candles.size());

    final var candlesProportions = retrieveCandlesProportions(candles);
    final var bodiesProportions = retrieveBodiesProportions(candles);
    final var volumeProportions = retrieveVolumeProportions(candles);
    final var positions = retrieveCandlesPositions(candles);


    for (int count = 0; count < candles.size(); count++) {
      final var comparison = CandleComparison.builder()
        .candleProportion(candlesProportions.get(count))
        .bodyProportion(bodiesProportions.get(count))
        .position(positions.get(count))
        .volumeProportion(volumeProportions.get(count))
        .build();

      final var candle = candles.get(count);

      final var newCandle = candle.toBuilder()
        .comparison(comparison)
        .build();

      result.add(newCandle);
    }

    return result;
  }

  private List<CandleProportion> retrieveCandlesProportions(List<Candle> candles) {
    return retrieveProportions(candles, Candle::getSize);
  }

  private List<CandleProportion> retrieveBodiesProportions(List<Candle> candles) {
    return retrieveProportions(candles, Candle::getBodySize);
  }

  private List<CandleProportion> retrieveVolumeProportions(List<Candle> candles) {
    return retrieveProportions(candles, Candle::getVolume);
  }

  private List<CandleProportion> retrieveProportions(List<Candle> candles, Function<Candle, BigDecimal> sizeGetter) {

    final var averageBodySize = statisticsUtil.calculateAverage(candles, sizeGetter);

    final var bodiesProportions = new ArrayList<CandleProportion>(candles.size());

    final var sizes = candles.stream()
      .map(sizeGetter)
      .toList();

    for (BigDecimal size : sizes) {

      final var ratio = size.compareTo(BigDecimal.ZERO) == 0 ?
        BigDecimal.ZERO : size.divide(averageBodySize, GeneralConfiguration.DEFAULT_ROUNDING_MODE);

      final var step = BigDecimal.valueOf(1.0 / (CandleProportion.values().length / 2.0));
      var index = ratio.divide(step, GeneralConfiguration.DEFAULT_ROUNDING_MODE);

      if (index.intValue() >= CandleProportion.values().length) {
        index = BigDecimal.valueOf(CandleProportion.values().length - 1L);
      }

      final var bodyProportion = CandleProportion.values()[index.intValue()];

      bodiesProportions.add(bodyProportion);
    }

    return bodiesProportions;
  }

  private List<CandlePosition> retrieveCandlesPositions(List<Candle> candles) {
    Candle previous = null;
    final var candlesPositions = new ArrayList<CandlePosition>(candles.size());

    final var averageStandardDeviation = statisticsUtil.calculateStandardDeviation(candles, Candle::getAverage);

    for (Candle candle : candles) {
      var candlePosition = CandlePosition.SAME_LEVEL;
      if (previous != null) {

        final var averageDifference = candle.getAverage()
          .subtract(previous.getAverage());

        final int comparisonResult = comparisonUtil.compareRatioUsingMargin(averageDifference, averageStandardDeviation);

        candlePosition = CandlePosition.findByComparisonResult(comparisonResult);
      }

      candlesPositions.add(candlePosition);
      previous = candle;
    }

    return candlesPositions;
  }

}
