package com.github.marceloleite2604.cryptotrader.dto.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class DoubleStdSerializer extends StdSerializer<Double> {

  private final DecimalFormat decimalFormat;

  public DoubleStdSerializer() {
    super(Double.class);
    this.decimalFormat = createDecimalFormat();
  }

  private DecimalFormat createDecimalFormat() {
    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols(Locale.US);
    final var result = new DecimalFormat("#.#", decimalFormatSymbols);
    result.setMaximumFractionDigits(8);
    return result;
  }

  @Override
  public void serialize(
    Double aDouble,
    JsonGenerator jsonGenerator,
    SerializerProvider serializerProvider) throws IOException {
    if (aDouble != null) {
      jsonGenerator.writeNumber(decimalFormat.format(aDouble.doubleValue()));
    }
  }
}
