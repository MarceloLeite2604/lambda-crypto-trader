package com.github.marceloleite2604.cryptotrader.mapper.mail;

import com.github.marceloleite2604.cryptotrader.mapper.Mapper;
import com.github.marceloleite2604.cryptotrader.model.Mail;
import com.github.marceloleite2604.cryptotrader.properties.MailProperties;
import com.github.marceloleite2604.cryptotrader.service.ActionService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class MailMapper<I> implements Mapper<I, Mail> {

  protected final MailProperties mailProperties;

  protected final ActionService actionService;


}
