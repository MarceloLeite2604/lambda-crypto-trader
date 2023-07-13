package com.github.marceloleite2604.cryptotrader.service;

import com.github.marceloleite2604.cryptotrader.model.Action;
import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.Side;
import com.github.marceloleite2604.cryptotrader.model.pattern.PatternMatch;
import com.github.marceloleite2604.cryptotrader.util.FormatUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActionService {

  private final FormatUtil formatUtil;

  public Action create(final Active active, final List<PatternMatch> patternMatches) {
    return create(active, patternMatches, null);
  }

  public Action create(final Active active, final List<PatternMatch> patternMatches, final BigDecimal profit) {
    Assert.notNull(active, "Active cannot be null.");
    Assert.notEmpty(patternMatches, "Pattern matches list cannot be null or empty.");

    final var side = patternMatches.get(0)
      .getType()
      .getSide();

    final var reasons = patternMatches.stream()
      .map(patternMatch -> elaborateReason(patternMatch, profit))
      .toList();

    return Action.builder()
      .side(side)
      .active(active)
      .reasons(reasons)
      .build();
  }

  private String elaborateReason(final PatternMatch patternMatch, final BigDecimal profit) {

    if (profit != null) {
      return String.format("%s pattern found at %s. Approximate profit is %s.", patternMatch.getType()
        .getName(), patternMatch.getCandleTime(), formatUtil.toPercentage(profit));
    }

    return String.format("%s pattern found at %s.", patternMatch.getType()
      .getName(), patternMatch.getCandleTime());
  }

  public List<Action> retrieveBySide(List<Action> actions, Side side) {
    if (CollectionUtils.isEmpty(actions)) {
      return Collections.emptyList();
    }

    return actions.stream()
      .filter(action -> side.equals(action.getSide()))
      .toList();
  }
}
