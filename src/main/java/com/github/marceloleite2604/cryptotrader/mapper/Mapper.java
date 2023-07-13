package com.github.marceloleite2604.cryptotrader.mapper;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public interface Mapper<I, O> {

  default O mapTo(I input) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  default List<O> mapAllTo(List<I> inputs) {
    if (CollectionUtils.isEmpty(inputs)) {
      return Collections.emptyList();
    }

    final List<O> output = new ArrayList<>(inputs.size());

    inputs.stream()
      .map(this::mapTo)
      .forEach(output::add);

    return output;
  }

  default I mapFrom(O output) {
    throw new UnsupportedOperationException("Not implemented.");
  }

  default List<I> mapAllFrom(List<O> outputs) {
    if (CollectionUtils.isEmpty(outputs)) {
      return Collections.emptyList();
    }

    final List<I> inputs = new ArrayList<>(outputs.size());

    outputs.stream()
      .map(this::mapFrom)
      .forEach(inputs::add);

    return inputs;
  }

}
