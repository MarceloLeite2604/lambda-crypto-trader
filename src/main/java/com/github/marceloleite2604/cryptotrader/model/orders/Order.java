package com.github.marceloleite2604.cryptotrader.model.orders;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Order implements Comparable<Order> {

  private final BigDecimal averagePrice;

  private final OffsetDateTime createdAt;

  private final List<Execution> executions;

  private final BigDecimal filledQuantity;

  private final String id;

  private final String instrument;

  private final BigDecimal limitPrice;

  private final BigDecimal quantity;

  private final String side;

  private final String status;

  private final String type;

  private final OffsetDateTime updatedAt;

  private final BigDecimal fee;

  private final String triggerOrderId;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Order order = (Order) o;
    return Objects.equals(createdAt, order.createdAt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(createdAt);
  }

  @Override
  public int compareTo(Order other) {
    return this.createdAt.compareTo(other.createdAt);
  }
}
