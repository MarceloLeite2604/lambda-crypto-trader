package com.github.marceloleite2604.cryptotrader.model.orders;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.time.OffsetDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RetrieveOrdersRequest {

  @NotBlank
  private final String accountId;

  @NotBlank
  private final String symbol;

  private final Boolean hasExecutions;

  private final String type;

  private final String status;

  private final String idFrom;

  private final String idTo;

  private final OffsetDateTime createdAtFrom;

  private final OffsetDateTime createdAtTo;
}
