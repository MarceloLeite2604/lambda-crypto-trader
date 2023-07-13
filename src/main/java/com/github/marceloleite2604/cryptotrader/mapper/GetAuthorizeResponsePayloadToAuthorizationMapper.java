package com.github.marceloleite2604.cryptotrader.mapper;

import com.github.marceloleite2604.cryptotrader.dto.authorize.Authorization;
import com.github.marceloleite2604.cryptotrader.dto.authorize.GetAuthorizeResponsePayload;
import com.github.marceloleite2604.cryptotrader.util.DateTimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class GetAuthorizeResponsePayloadToAuthorizationMapper implements
  Mapper<GetAuthorizeResponsePayload, Authorization> {

  private final DateTimeUtil dateTimeUtil;

  @Override
  public Authorization mapTo(GetAuthorizeResponsePayload getAuthorizeResponsePayload) {
    return Authorization.builder()
      .token(getAuthorizeResponsePayload.getAccessToken())
      .expiration(
        dateTimeUtil.convertEpochToUtcOffsetDateTime(
          getAuthorizeResponsePayload.getExpiration()))
      .build();
  }
}
