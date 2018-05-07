package com.wind.oauth;

import lombok.Data;

@Data
public class OAuth2ClientProperties {
    private String clientId;
    private String clientSecret;
    private Integer accessTokenValiditySeconds = 60 * 60;
    private Integer refreshTokenValiditySeconds = 60 * 60 * 30 * 24;
}