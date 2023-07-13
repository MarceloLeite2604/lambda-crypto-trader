package com.github.marceloleite2604.cryptotrader.service.actionexecutor;

import com.github.marceloleite2604.cryptotrader.mapper.mail.ListActionsToMailMapper;
import com.github.marceloleite2604.cryptotrader.mapper.mail.MailToMimeMessageMapper;
import com.github.marceloleite2604.cryptotrader.mapper.mail.ThrowableToMailMapper;
import com.github.marceloleite2604.cryptotrader.model.Action;
import com.github.marceloleite2604.cryptotrader.model.Mail;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.Transport;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MailService implements ActionExecutor {

  private final ListActionsToMailMapper listActionsToMailMapper;

  private final MailToMimeMessageMapper mailToMimeMessageMapper;

  private final ThrowableToMailMapper throwableToMailMapper;

  @SneakyThrows
  public void execute(List<Action> actions) {
    final var mail = listActionsToMailMapper.mapTo(actions);
    sendMail(mail);
  }

  @SneakyThrows
  public void send(Throwable throwable) {
    final var mail = throwableToMailMapper.mapTo(throwable);
    sendMail(mail);
  }

  private void sendMail(Mail mail) throws MessagingException {
    final var mimeMessage = mailToMimeMessageMapper.mapTo(mail);
    Transport.send(mimeMessage);
  }

}
