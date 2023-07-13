package com.github.marceloleite2604.cryptotrader.dto.candle;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@Getter
public class GetCandleResponsePayload {

  @JsonAlias("c")
  private final List<Double> close;

  @JsonAlias("h")
  private final List<Double> high;

  @JsonAlias("l")
  private final List<Double> low;

  @JsonAlias("o")
  private final List<Double> open;

  @JsonAlias("t")
  private final List<Long> timestamp;

  @JsonAlias("v")
  private final List<Double> volume;

  private Map<String, Object> additionalProperties;

  @JsonAnySetter
  public void add(String key, Object value) {
    if (additionalProperties == null) {
      additionalProperties = new HashMap<>();
    }
    additionalProperties.put(key, value);
  }
}
