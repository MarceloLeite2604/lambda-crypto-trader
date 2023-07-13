package com.github.marceloleite2604.cryptotrader.dto.tickers;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@Getter
public class TickerDto {

  private final String buy;

  private final long date;

  private final String high;

  private final String last;

  private final String low;

  private final String open;

  private final String pair;

  private final String sell;

  private final String vol;

  private Map<String, Object> additionalProperties;

  @JsonAnySetter
  public void add(String key, Object value) {
    if (additionalProperties == null) {
      additionalProperties = new HashMap<>();
    }
    additionalProperties.put(key, value);
  }
}
