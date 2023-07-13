package com.github.marceloleite2604.cryptotrader.dto.account;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@Getter
public class PositionDto {

  private final double avgPrice;

  private final String id;

  private final String instrument;

  private final double quantity;

  private final String side;

  private Map<String, Object> additionalProperties;

  @JsonAnySetter
  public void add(String key, Object value) {
    if (additionalProperties == null) {
      additionalProperties = new HashMap<>();
    }
    additionalProperties.put(key, value);
  }
}
