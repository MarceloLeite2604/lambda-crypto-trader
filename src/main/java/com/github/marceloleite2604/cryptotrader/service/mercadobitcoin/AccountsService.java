package com.github.marceloleite2604.cryptotrader.service.mercadobitcoin;

import com.github.marceloleite2604.cryptotrader.dto.account.AccountDto;
import com.github.marceloleite2604.cryptotrader.mapper.AccountDtoMapper;
import com.github.marceloleite2604.cryptotrader.model.account.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@RequiredArgsConstructor
@Component
class AccountsService {

  private final WebClient mbAuthenticatedWebClient;

  private final AccountDtoMapper accountDtoMapper;

  public List<Account> retrieve() {
    final var accountDtos = mbAuthenticatedWebClient.get()
      .uri("accounts")
      .retrieve()
      .bodyToMono(new ParameterizedTypeReference<List<AccountDto>>() {
      })
      .blockOptional()
      .orElseThrow(() -> new IllegalStateException("Could not retrieve all accounts."));

    return accountDtoMapper.mapAllTo(accountDtos);
  }

}
