package com.github.marceloleite2604.cryptotrader.mapper.mail;

import com.github.marceloleite2604.cryptotrader.mapper.Mapper;
import com.github.marceloleite2604.cryptotrader.model.Mail;
import com.github.marceloleite2604.cryptotrader.properties.MailProperties;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Component
@RequiredArgsConstructor
public class MailToMimeMessageMapper implements Mapper<Mail, MimeMessage> {

  private final MailProperties mailProperties;

  private final CustomAuthenticator customAuthenticator;

  private Session session;

  @SneakyThrows
  @Override
  public MimeMessage mapTo(Mail mail) {
    final var mimeMessage = new MimeMessage(getSession());
    final var from = elaborateInternetAddress(mail.getFrom());

    mimeMessage.setFrom(from);

    mail.getRecipients()
      .stream()
      .map(this::elaborateInternetAddress)
      .forEach(recipient -> addRecipient(mimeMessage, recipient));

    mimeMessage.setSubject(mail.getSubject());
    mimeMessage.setText(mail.getText());

    return mimeMessage;
  }

  @SneakyThrows
  private void addRecipient(MimeMessage mimeMessage, InternetAddress recipient) {
    mimeMessage.addRecipient(Message.RecipientType.TO, recipient);
  }

  private Session getSession() {
    if (session == null) {
      final var properties = createProperties();
      session = Session.getInstance(properties, customAuthenticator);
    }
    return session;
  }

  private Properties createProperties() {
    final var properties = new Properties();
    properties.put("mail.smtp.host", mailProperties.getHost());
    properties.put("mail.smtp.port", mailProperties.getPort());
    properties.put("mail.smtp.ssl.enable", "true");
    properties.put("mail.smtp.auth", "true");
    return properties;
  }

  @SneakyThrows
  private InternetAddress elaborateInternetAddress(String address) {
    return new InternetAddress(address);
  }
}
