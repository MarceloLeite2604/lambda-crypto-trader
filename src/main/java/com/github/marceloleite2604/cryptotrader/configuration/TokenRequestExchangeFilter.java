package com.github.marceloleite2604.cryptotrader.configuration;

import com.github.marceloleite2604.cryptotrader.dto.authorize.Authorization;
import com.github.marceloleite2604.cryptotrader.dto.authorize.GetAuthorizeResponsePayload;
import com.github.marceloleite2604.cryptotrader.mapper.GetAuthorizeResponsePayloadToAuthorizationMapper;
import com.github.marceloleite2604.cryptotrader.properties.MercadoBitcoinProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Component
@Slf4j
public class TokenRequestExchangeFilter implements ExchangeFilterFunction {

  public static final String HTTP_HEADER_AUTHORIZATION_NAME = "Authorization";

  private final WebClient mbUnauthenticatedWebClient;
  private final GetAuthorizeResponsePayloadToAuthorizationMapper getAuthorizeResponsePayloadToAuthorizationMapper;
  private final BodyInserters.FormInserter<String> credentialsFormInserter;
  private Authorization authorization;

  public TokenRequestExchangeFilter(
    WebClient mbUnauthenticatedWebClient,
    GetAuthorizeResponsePayloadToAuthorizationMapper getAuthorizeResponsePayloadToAuthorizationMapper,
    MercadoBitcoinProperties mercadoBitcoinProperties
  ) {
    this.mbUnauthenticatedWebClient = mbUnauthenticatedWebClient;
    this.getAuthorizeResponsePayloadToAuthorizationMapper = getAuthorizeResponsePayloadToAuthorizationMapper;
    this.credentialsFormInserter = createCredentialsFormInserter(mercadoBitcoinProperties);
  }

  private BodyInserters.FormInserter<String> createCredentialsFormInserter(
    MercadoBitcoinProperties mercadoBitcoinProperties) {
    return BodyInserters.fromFormData("login", mercadoBitcoinProperties.getClientId())
      .with("password", mercadoBitcoinProperties.getClientSecret());
  }

  @Override
  public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
    if (isNotValid(authorization)) {
      authorization = retrieveAuthorization();
    }

    final var clientRequest = ClientRequest.from(request)
      .header(HTTP_HEADER_AUTHORIZATION_NAME, authorization.getToken())
      .build();
    return next.exchange(clientRequest);
  }

  private Authorization retrieveAuthorization() {
    return mbUnauthenticatedWebClient.post()
      .contentType(MediaType.MULTIPART_FORM_DATA)
      .body(credentialsFormInserter)
      .retrieve()
      .bodyToMono(GetAuthorizeResponsePayload.class)
      .blockOptional()
      .map(getAuthorizeResponsePayloadToAuthorizationMapper::mapTo)
      .orElseThrow(() -> new IllegalStateException("Error while authenticating with Mercado Bitcoin."));
  }

  private boolean isNotValid(Authorization authorization) {
    return authorization == null ||
      OffsetDateTime.now(ZoneOffset.UTC)
        .compareTo(authorization.getExpiration()) > 0;
  }
}
