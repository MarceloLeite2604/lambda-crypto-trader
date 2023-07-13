package com.github.marceloleite2604.cryptotrader.model.account;

import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.Side;
import com.github.marceloleite2604.cryptotrader.model.orders.Order;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

  private final String currency;

  private final String currencySign;

  private final String id;

  private final String name;

  private final String type;

  private final Map<Active, Balance> balances;

  private final Map<Active, List<Order>> orders;

  public Balance retrieveBalanceFor(final Active active) {
    Assert.notNull(active, "Active cannot be empty.");

    final var balance = balances.get(active);

    if (balance == null) {
      final var message = String.format("Balance for \"%s\" is null", active.getName());
      throw new IllegalStateException(message);
    }

    return balance;
  }

  public Optional<Order> retrieveLastOrderFor(final Active active) {
    Assert.notNull(active, "Active cannot be null.");

    final var activeOrders = orders.getOrDefault(active, Collections.emptyList());

    if (CollectionUtils.isEmpty(activeOrders)) {
      return Optional.empty();
    }

    activeOrders.sort(Comparator.reverseOrder());
    final var order = activeOrders.get(0);

    return Optional.of(order);
  }

  public Optional<Order> retrieveLastSideOrder(final Active active, final Side side) {
    Assert.notNull(active, "Active cannot be null.");
    Assert.notNull(side, "Side cannot be null.");

    final var activeOrders = orders.getOrDefault(active, Collections.emptyList());

    if (CollectionUtils.isEmpty(activeOrders)) {
      return Optional.empty();
    }

    final var sideOrders = activeOrders.stream()
      .filter(order -> order.getSide()
        .equalsIgnoreCase(side.name()))
      .collect(Collectors.toCollection(ArrayList::new));

    if (CollectionUtils.isEmpty(sideOrders)) {
      return Optional.empty();
    }

    sideOrders.sort(Comparator.reverseOrder());

    return Optional.of(sideOrders.get(0));
  }
}
