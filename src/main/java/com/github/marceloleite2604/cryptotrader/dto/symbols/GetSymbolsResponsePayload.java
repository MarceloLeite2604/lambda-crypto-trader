package com.github.marceloleite2604.cryptotrader.dto.symbols;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@NoArgsConstructor(force = true)
public class GetSymbolsResponsePayload {

  @JsonProperty("base-currency")
  private final List<String> baseCurrency;

  private final List<String> currency;

  private final List<String> description;

  @JsonProperty("exchange-listed")
  private final List<Boolean> exchangeListed;

  @JsonProperty("exchange-traded")
  private final List<Boolean> exchangeTraded;

  @JsonProperty("minmovement")
  private final List<Double> minMovement;

  @JsonProperty("pricescale")
  private final List<Long> priceScale;

  @JsonProperty("session-regular")
  private final List<String> sessionRegular;

  private final List<String> symbol;

  private final List<String> timezone;

  private final List<String> type;

  private Map<String, Object> additionalProperties;

  @JsonAnySetter
  public void add(String key, Object value) {
    if (additionalProperties == null) {
      additionalProperties = new HashMap<>();
    }
    additionalProperties.put(key, value);
  }
}
