package com.github.marceloleite2604.cryptotrader.configuration;

import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.service.ActiveService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
@Slf4j
public class LoadActivesCommandLineRunner implements CommandLineRunner {

  private final ActiveService activeService;

  @Override
  public void run(String... args) {
    activeService.retrieve();
    if (log.isInfoEnabled()) {
      final var length = Active.values().length;
      final var text = length > 1 ? "actives" : "active";
      log.info("{} {} loaded.", length, text);
    }

  }
}
