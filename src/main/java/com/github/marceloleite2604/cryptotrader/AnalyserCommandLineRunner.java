package com.github.marceloleite2604.cryptotrader;

import com.github.marceloleite2604.cryptotrader.service.AccountService;
import com.github.marceloleite2604.cryptotrader.service.AnalyserService;
import com.github.marceloleite2604.cryptotrader.service.actionexecutor.ActionExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(2)
public class AnalyserCommandLineRunner implements CommandLineRunner {

  private final AccountService accountService;

  private final AnalyserService analyserService;

  private final ActionExecutor mailService;

  @Override
  public void run(String... args) {

    final var account = accountService.retrieve();

    final var actions = analyserService.analyse(account);

    mailService.execute(actions);

  }
}
