package com.github.marceloleite2604.cryptotrader.properties;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PropertiesPath {

  private final String BASE_PATH = "crypto-trader";

  public final String MERCADO_BITCOIN = BASE_PATH + ".mercado-bitcoin";

  public static final String MAIL = BASE_PATH + ".mail";

  public static final String MONITORING = BASE_PATH + ".monitoring";
}
