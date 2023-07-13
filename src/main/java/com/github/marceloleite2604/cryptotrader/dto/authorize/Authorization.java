package com.github.marceloleite2604.cryptotrader.dto.authorize;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Authorization {

    private final String token;

    private final OffsetDateTime expiration;
}
