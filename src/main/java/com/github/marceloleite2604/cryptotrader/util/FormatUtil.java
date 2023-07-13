package com.github.marceloleite2604.cryptotrader.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

@Component
public class FormatUtil {

  private final NumberFormat brlNumberFormat;

  private final NumberFormat percentageFormat;

  public FormatUtil() {
    this.brlNumberFormat = createBrlNumberFormat();
    this.percentageFormat = createPercentageFormat();
  }

  private NumberFormat createPercentageFormat() {
    final var result = NumberFormat.getPercentInstance();
    result.setMinimumFractionDigits(2);
    return result;
  }

  private NumberFormat createBrlNumberFormat() {
    NumberFormat result = NumberFormat.getCurrencyInstance(Locale.forLanguageTag("pt-BR"));
    result.setMinimumFractionDigits(2);
    result.setMaximumFractionDigits(2);
    return result;
  }

  public String toBrl(BigDecimal bigDecimal) {
    return brlNumberFormat.format(bigDecimal.doubleValue());
  }

  public String toPercentage(BigDecimal bigDecimal) {
    return percentageFormat.format(bigDecimal.doubleValue());
  }
}
