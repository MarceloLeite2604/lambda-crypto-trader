package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.account.AccountDto;
import com.github.marceloleite2604.cryptotrader.model.account.Account;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class AccountDtoMapper implements Mapper<AccountDto, Account> {

  @Override
  public Account mapTo(AccountDto accountDto) {
    return Account.builder()
      .currency(accountDto.getCurrency())
      .currencySign(accountDto.getCurrencySign())
      .id(accountDto.getId())
      .name(accountDto.getName())
      .type(accountDto.getType())
      .balances(Collections.emptyMap())
      .orders(Collections.emptyMap())
      .build();
  }
}
