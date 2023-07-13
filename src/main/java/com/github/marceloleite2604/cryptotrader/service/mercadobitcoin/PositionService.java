package com.github.marceloleite2604.cryptotrader.service.mercadobitcoin;

import com.github.marceloleite2604.cryptotrader.dto.account.PositionDto;
import com.github.marceloleite2604.cryptotrader.mapper.PositionDtoMapper;
import com.github.marceloleite2604.cryptotrader.model.account.Position;
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
class PositionService {

  private final WebClient mbAuthenticatedWebClient;

  private final PositionDtoMapper positionDtoMapper;

  public List<Position> retrieve(String accountId, String symbol) {
    final var getBalanceUri = buildGetPositionsUri(accountId, symbol);

    final var positionDtos = mbAuthenticatedWebClient.get()
      .uri(getBalanceUri)
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<List<PositionDto>>() {
      })
      .blockOptional()
      .orElseThrow(() -> new IllegalStateException("Could not retrieve all account positions."));

    return positionDtoMapper.mapAllTo(positionDtos);
  }

  @SneakyThrows
  private String buildGetPositionsUri(String accountId, String symbol) {
    Assert.isTrue(StringUtils.isNotBlank(accountId), "Account ID cannot be blank.");
    Assert.isTrue(StringUtils.isNotBlank(symbol), "Symbol cannot be blank.");

    final var getBalanceUriBuilder = new URIBuilder()
      .setPathSegments("accounts", accountId, "positions")
      .addParameter("symbol", symbol);

    return getBalanceUriBuilder.build()
      .toString();
  }
}
