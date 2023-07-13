package com.github.marceloleite2604.cryptotrader.dto.orders;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor(force = true)
@Getter
public class PostOrderRequestPayload {

  private final Boolean async;

  private final Double cost;

  private final Double limitPrice;

  private final Double qty;

  private final String side;

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
