package com.github.marceloleite2604.cryptotrader.dto.trades;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@Getter
public class TradeDto {

  private final double amount;

  private final long date;

  private final double price;

  private final long tid;

  private final String type;

  private Map<String, Object> additionalProperties;

  @JsonAnySetter
  public void add(String key, Object value) {
    if (additionalProperties == null) {
      additionalProperties = new HashMap<>();
    }
    additionalProperties.put(key, value);
  }

}
