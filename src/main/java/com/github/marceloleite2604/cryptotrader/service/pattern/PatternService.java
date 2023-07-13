package com.github.marceloleite2604.cryptotrader.service.pattern;

import com.github.marceloleite2604.cryptotrader.model.Side;
import com.github.marceloleite2604.cryptotrader.model.candles.Candle;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternCheckContext;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternMatch;
import com.github.marceloleite2604.cryptotrader.service.pattern.links.PatternChecker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class PatternService {

  private final PatternChecker first;

  public PatternService(List<PatternChecker> patternCheckers) {
    this.first = createChain(patternCheckers);
  }

  private PatternChecker createChain(List<PatternChecker> patternCheckers) {
    PatternChecker firstElement = null;
    PatternChecker previous = null;

    for (PatternChecker patternChecker : patternCheckers) {
      if (firstElement == null) {
        firstElement = patternChecker;
      } else {
        previous.setNext(patternChecker);
      }
      previous = patternChecker;
    }

    return firstElement;
  }

  public List<PatternMatch> check(List<Candle> candles) {
    if (CollectionUtils.isEmpty(candles)) {
      return Collections.emptyList();
    }

    if (first == null) {
      return Collections.emptyList();
    }

    var patternCheckContext = PatternCheckContext.builder()
      .candles(candles)
      .build();

    candles.sort(Collections.reverseOrder());
    patternCheckContext = first.check(patternCheckContext);
    Collections.sort(candles);

    return patternCheckContext.getPatternMatches();
  }

  public List<PatternMatch> findPatternMatchesBySide(List<PatternMatch> patternMatches, Side side) {
    if (CollectionUtils.isEmpty(patternMatches)) {
      return Collections.emptyList();
    }

    return patternMatches.stream()
      .filter(patternMatch -> patternMatch.getType()
        .getSide()
        .equals(side))
      .toList();
  }
}
