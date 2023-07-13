package com.github.marceloleite2604.cryptotrader.service.mercadobitcoin;

import com.github.marceloleite2604.cryptotrader.dto.account.BalanceDto;
import com.github.marceloleite2604.cryptotrader.mapper.BalanceDtoMapper;
import com.github.marceloleite2604.cryptotrader.model.account.Balance;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@RequiredArgsConstructor
class BalanceService {

  private final WebClient mbAuthenticatedWebClient;

  private final BalanceDtoMapper balanceDtoMapper;

  public List<Balance> retrieve(String accountId, String symbol) {
    final var getBalanceUri = buildGetBalancesUri(accountId, symbol);

    final var balanceDtos = mbAuthenticatedWebClient.get()
      .uri(getBalanceUri)
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<List<BalanceDto>>() {
      })
      .blockOptional()
      .orElseThrow(() -> new IllegalStateException("Could not retrieve all account balances."));

    return balanceDtoMapper.mapAllTo(balanceDtos);
  }

  @SneakyThrows
  private String buildGetBalancesUri(String accountId, String symbol) {
    Assert.isTrue(StringUtils.isNotBlank(accountId), "Account ID cannot be blank.");
    Assert.isTrue(StringUtils.isNotBlank(symbol), "Symbol cannot be blank.");

    return new URIBuilder().setPathSegments("accounts", accountId, "balances")
      .addParameter("symbol", symbol)
      .build()
      .toString();
  }
}
