package com.wind.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "wechat.miniapp")
public class MiniAppProperties {
    /**
     * 设置微信小程序的appid
     */
    private String id;

    /**
     * 设置微信小程序的Secret
     */
    private String secret;

    /**
     * 设置微信小程序的token
     */
    private String token;

    /**
     * 设置微信小程序的EncodingAESKey
     */
    private String aesKey;

    /**
     * 消息格式，XML或者JSON
     */
    private String messageFormat;
}
