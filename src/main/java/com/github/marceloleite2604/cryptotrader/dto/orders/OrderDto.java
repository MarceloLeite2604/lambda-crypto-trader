package com.github.marceloleite2604.cryptotrader.dto.orders;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@Getter
public class OrderDto {

  private final double avgPrice;

  @JsonProperty("created_at")
  private final long createdAt;

  private final List<ExecutionDto> executions;

  private final double filledQty;

  private final String id;

  private final String instrument;

  private final double limitPrice;

  private final double qty;

  private final String side;

  private final String status;

  private final String type;

  @JsonProperty("updated_at")
  private final long updatedAt;

  private final double fee;

  private final String triggerOrderId;

  private Map<String, Object> additionalProperties;

  @JsonAnySetter
  public void add(String key, Object value) {
    if (additionalProperties == null) {
      additionalProperties = new HashMap<>();
    }
    additionalProperties.put(key, value);
  }
}
