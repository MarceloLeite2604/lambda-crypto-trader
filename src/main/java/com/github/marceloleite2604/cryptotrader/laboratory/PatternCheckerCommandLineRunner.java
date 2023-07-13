package com.github.marceloleite2604.cryptotrader.laboratory;

import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.Side;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlePrecision;
import com.github.marceloleite2604.cryptotrader.model.candles.CandlesRequest;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternMatch;
import com.github.marceloleite2604.cryptotrader.service.candle.CandleComparisonService;
import com.github.marceloleite2604.cryptotrader.service.candle.CandleService;
import com.github.marceloleite2604.cryptotrader.service.pattern.PatternService;
import com.github.marceloleite2604.cryptotrader.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.CommandLineRunner;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
//@Component
@Slf4j
public class PatternCheckerCommandLineRunner implements CommandLineRunner {

  private final CandleService candleService;

  private final DateTimeUtil dateTimeUtil;

  private final PatternService patternService;

  private final CandleComparisonService candleComparisonService;


  @Override
  public void run(String... args) {
    final var listSize = 12;
    final var active = Active.findBySymbol("BTC");
    final var precision = CandlePrecision.ONE_DAY;
    final var duration = precision.getDuration();
    final var to = dateTimeUtil.truncateTo(
      OffsetDateTime.now(ZoneOffset.UTC),
      duration);
    final var from = to.minus(duration.multipliedBy(200));

    final var candlesRequest = CandlesRequest.builder()
      .resolution(precision)
      .from(from)
      .toTime(to)
      .active(active)
      .build();

    final var candles = candleService.retrieveCandles(candlesRequest);

    List<PatternMatch> patternMatches = new ArrayList<>();

    for (int count = candles.size(); count >= listSize; count--) {
      var selectedCandles = candles.subList(count - listSize, count);
      selectedCandles = candleComparisonService.compare(selectedCandles);
      selectedCandles.sort(Comparator.reverseOrder());
      final var patternMatchesForSelectedCandles = patternService.check(selectedCandles);
      patternMatches.addAll(patternMatchesForSelectedCandles);
    }

    if (CollectionUtils.isNotEmpty(patternMatches)) {
      patternMatches.sort(Comparator.comparing(PatternMatch::getCandleTime));
      log.info("The following patterns were found:");
      patternMatches.forEach(patternMatch -> log.info("{}: {}", patternMatch.getCandleTime(), patternMatch.getType()));

      patternMatches.stream()
        .collect(Collectors.groupingBy(PatternMatch::getType, Collectors.counting()))
        .forEach((type, count) -> log.info("{}: {}", type, count));

      log.info("Total of patterns found:");
      patternMatches.stream()
        .collect(Collectors.groupingBy(PatternMatch::getType, Collectors.counting()))
        .forEach((type, count) -> log.info("{}: {}", type, count));

      final var patternMatchesByDay = patternMatches.stream()
        .collect(Collectors.groupingBy(patternMatch ->
          patternMatch.getCandleTime()
            .truncatedTo(ChronoUnit.DAYS)));

      Map<OffsetDateTime, Map<Side, Long>> sideCountingByDay = new HashMap<>();
      for (Map.Entry<OffsetDateTime, List<PatternMatch>> entry : patternMatchesByDay.entrySet()) {
        final var sideCountingMap = entry.getValue()
          .stream()
          .collect(Collectors.groupingBy(patternMatch -> patternMatch.getType()
            .getSide(), Collectors.counting()));
        sideCountingByDay.put(entry.getKey(), sideCountingMap);
      }

      final var days = new ArrayList<>(sideCountingByDay.keySet());

      Collections.sort(days);

      for (OffsetDateTime day : days) {
        final var sidesCounting = sideCountingByDay.get(day);
        log.info("{}: {}",
          day,
          sidesCounting);
      }
    }
  }
}

