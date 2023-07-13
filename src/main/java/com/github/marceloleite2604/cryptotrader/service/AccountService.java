package com.github.marceloleite2604.cryptotrader.service;

import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.account.Account;
import com.github.marceloleite2604.cryptotrader.model.orders.RetrieveOrdersRequest;
import com.github.marceloleite2604.cryptotrader.properties.MonitoringProperties;
import com.github.marceloleite2604.cryptotrader.service.mercadobitcoin.MercadoBitcoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {

  private final MercadoBitcoinService mercadoBitcoinService;

  private final MonitoringProperties monitoringProperties;

  public Account retrieve() {
    final var account = mercadoBitcoinService.retrieveAccount();

    final var actives = monitoringProperties.getActives()
      .stream()
      .map(Active::findByBase)
      .toList();

    final var balances = actives.stream()
      .collect(Collectors.toMap(
        Function.identity(),
        active -> mercadoBitcoinService.retrieveBalance(account.getId(), active)));

    final var orders = actives.stream()
      .collect(Collectors.toMap(
        Function.identity(),
        active -> {
          final var retrieveOrdersRequest = RetrieveOrdersRequest.builder()
            .accountId(account.getId())
            .symbol(active.getSymbol())
            .build();
          return mercadoBitcoinService.retrieveOrders(retrieveOrdersRequest);
        }));

    return account.toBuilder()
      .balances(balances)
      .orders(orders)
      .build();

  }

}
