package com.github.marceloleite2604.cryptotrader.configuration;

import com.github.marceloleite2604.cryptotrader.service.actionexecutor.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ExitCodeExceptionMapper;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class CryptoTraderExitCodeExceptionMapper implements ExitCodeExceptionMapper {

  private final MailService mailService;

  @Override
  public int getExitCode(Throwable throwable) {

    try {
      log.warn("Program finished abnormally. Sending email with exception thrown.");
      mailService.send(throwable);
    } catch (Exception exception) {
      log.error("Exception thrown while sending exception through email.", exception);
      log.error("The following exception was thrown during the program execution.", exception);
    }

    return 1;
  }
}
