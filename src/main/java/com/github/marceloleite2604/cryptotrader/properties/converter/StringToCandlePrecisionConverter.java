package com.github.marceloleite2604.cryptotrader.properties.converter;

import com.github.marceloleite2604.cryptotrader.model.candles.CandlePrecision;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@ConfigurationPropertiesBinding
public class StringToCandlePrecisionConverter implements Converter<String, CandlePrecision> {

  @Override
  public CandlePrecision convert(String value) {
    return CandlePrecision.findByValue(value);
  }
}
