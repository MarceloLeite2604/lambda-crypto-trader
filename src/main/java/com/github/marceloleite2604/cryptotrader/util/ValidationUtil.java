package com.github.marceloleite2604.cryptotrader.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class ValidationUtil {

  private final Validator validator;

  public <T> void throwIllegalArgumentExceptionIfNotValid(T object, String message) {
    if (object == null) {
      return;
    }

    Set<ConstraintViolation<T>> violations = validator.validate(object);

    if (CollectionUtils.isNotEmpty(violations)) {
      final var constraintViolationMessages = violations.stream()
        .map(constraintViolation ->
          constraintViolation.getPropertyPath()
            .toString() + ": " + constraintViolation.getMessage())
        .collect(Collectors.joining("\n\t"));

      final var exceptionMessage = message + "\n\t" +
        constraintViolationMessages;

      throw new IllegalArgumentException(exceptionMessage);
    }
  }
}
