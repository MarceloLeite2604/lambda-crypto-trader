package com.github.marceloleite2604.cryptotrader.dto.authorize;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(force = true)
@Getter
public class GetAuthorizeResponsePayload {

    @JsonProperty("access_token")
    private final String accessToken;

    private final long expiration;
}
