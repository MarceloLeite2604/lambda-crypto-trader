package com.github.marceloleite2604.cryptotrader.laboratory;

import com.github.marceloleite2604.cryptotrader.model.Action;
import com.github.marceloleite2604.cryptotrader.model.Active;
import com.github.marceloleite2604.cryptotrader.model.Side;
import com.github.marceloleite2604.cryptotrader.service.actionexecutor.MailService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;

import java.util.List;

//@Component
@RequiredArgsConstructor
public class MailSenderCommandLineRunner implements CommandLineRunner {

  private final MailService mailService;

  @Override
  public void run(String... args) {
    final var sellEthAction = Action.builder()
      .active(Active.findBySymbol("ETC"))
      .side(Side.SELL)
      .reasons(List.of(
        "The bluetooth phone just disconnected.",
        "The the building corridor exhaust does not stop.",
        "I have a pile of pillows over my computer."))
      .build();

    final var sellBtcAction = Action.builder()
      .active(Active.findBySymbol("BTC"))
      .side(Side.SELL)
      .reasons(List.of(
        "This empty bottle makes noise when I squeeze it with my hands.",
        "The heat is overwhelming.",
        "I am hungry."))
      .build();

    final var buyLtcAction = Action.builder()
      .active(Active.findBySymbol("LTC"))
      .side(Side.BUY)
      .reasons(List.of(
        "My notebook is always almost fully charged.",
        "I need to go to the bathroom.",
        "It is past midnight and I should be sleeping."))
      .build();

    final var buyEthAction = Action.builder()
      .active(Active.findBySymbol("ETH"))
      .side(Side.BUY)
      .reasons(List.of(
        "This action is conflicting with sell Ethereum.",
        "I should watch more Top Gear.",
        "There is a book at my beside table that I should be reading."))
      .build();

    final var actions = List.of(sellBtcAction, sellBtcAction, buyEthAction, buyLtcAction);

    mailService.execute(actions);
  }
}
