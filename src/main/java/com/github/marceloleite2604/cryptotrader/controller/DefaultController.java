package com.github.marceloleite2604.cryptotrader.controller;

import com.github.marceloleite2604.cryptotrader.service.AccountService;
import com.github.marceloleite2604.cryptotrader.service.AnalyserService;
import com.github.marceloleite2604.cryptotrader.service.actionexecutor.ActionExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DefaultController {

  private final AccountService accountService;

  private final AnalyserService analyserService;

  private final ActionExecutor mailService;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public void get() {

    final var account = accountService.retrieve();

    final var actions = analyserService.analyse(account);

    mailService.execute(actions);
  }
}
