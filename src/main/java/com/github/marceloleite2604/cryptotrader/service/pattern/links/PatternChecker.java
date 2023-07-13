package com.github.marceloleite2604.cryptotrader.service.pattern.links;

import com.github.marceloleite2604.cryptotrader.model.pattern.PatternCheckContext;

public interface PatternChecker {

  PatternCheckContext check(PatternCheckContext patternCheckContext);

  void setNext(PatternChecker patternChecker);
}
