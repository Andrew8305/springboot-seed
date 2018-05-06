package com.wind.oauth;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "oauth2")
public class OAuth2Properties {
    private String jwtSigningKey;
    private OAuth2ClientProperties[] clients;
}