package com.github.marceloleite2604.cryptotrader.mapper.mail;

import com.github.marceloleite2604.cryptotrader.model.Mail;
import com.github.marceloleite2604.cryptotrader.properties.MailProperties;
import com.github.marceloleite2604.cryptotrader.service.ActionService;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

@Component
public class ThrowableToMailMapper extends MailMapper<Throwable> {

  public static final String SUBJECT = "\uD83D\uDEA8️Something went wrong while executing Crypto trader.";

  public ThrowableToMailMapper(MailProperties mailProperties, ActionService actionService) {
    super(mailProperties, actionService);
  }

  @Override
  public Mail mapTo(Throwable throwable) {
    final var from = mailProperties.getUsername();

    final var recipients = mailProperties.getRecipients();

    final var text = elaborateText(throwable);

    return Mail.builder()
      .from(from)
      .recipients(recipients)
      .subject(SUBJECT)
      .text(text)
      .build();
  }

  private String elaborateText(Throwable throwable) {
    return "Crypto trader finished abnormally. Here is the exception stack trace.\n\n" +
      ExceptionUtils.getStackTrace(throwable) + "\n\n" +
      "By the way: Do not blame me, I am only the messenger! \uD83D\uDE05";
  }
}
