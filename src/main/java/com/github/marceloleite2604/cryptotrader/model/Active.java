package com.github.marceloleite2604.cryptotrader.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Slf4j
public class Active {

  public static final Active BRL = Active.builder()
    .base("BRL")
    .quote("BRL")
    .name("Brazilian Real")
    .build();

  public static final Active UNKNOWN = Active.builder()
    .base("UNKNOWN")
    .quote("UNKNOWN")
    .name("Unknown active")
    .build();

  private static final List<Active> ACTIVES = new ArrayList<>(List.of(BRL, UNKNOWN));

  private final String base;

  private final String quote;

  private final String name;

  public static void add(Active active) {
    ACTIVES.add(active);
  }

  public static Active[] values() {
    return ACTIVES.toArray(Active[]::new);
  }

  public static Active findBySymbol(String symbol) {
    if (symbol == null) {
      return null;
    }

    return ACTIVES.stream()
      .filter(active -> active.getSymbol()
        .equals(symbol))
      .findFirst()
      .orElseGet(() -> {
        log.warn("Could not find active for symbol \"{}}\".", symbol);
        return UNKNOWN;
      });
  }

  public static Active findByBase(String base) {
    if (base == null) {
      return null;
    }

    return ACTIVES.stream()
      .filter(active -> active.getBase()
        .equals(base))
      .findFirst()
      .orElseGet(() -> {
        log.warn("Could not find active for base \"{}\".", base);
        return UNKNOWN;
      });
  }

  public String getSymbol() {
    return base + "-" + quote;
  }
}
