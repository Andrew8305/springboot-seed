package com.wind.web.api;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaMessage;
import cn.binarywang.wx.miniapp.constant.WxMaConstants;
import cn.binarywang.wx.miniapp.message.WxMaMessageRouter;
import com.wind.common.SecurityUser;
import com.wind.mybatis.pojo.User;
import com.wind.web.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Objects;

@CommonsLog
@RestController
@RequestMapping("/api/mini_app" )
public class MiniAppAPI {

    @Autowired
    private UserService userService;

    @Autowired
    private WxMaService wxService;

    @Autowired
    private WxMaMessageRouter router;

    @GetMapping(produces = "text/plain;charset=utf-8" )
    public String authGet(@RequestParam(name = "signature", required = false) String signature,
                          @RequestParam(name = "timestamp", required = false) String timestamp,
                          @RequestParam(name = "nonce", required = false) String nonce,
                          @RequestParam(name = "echostr", required = false) String echostr) {
        if (StringUtils.isAnyBlank(signature, timestamp, nonce, echostr)) {
            throw new IllegalArgumentException("invalid argument" );
        }

        if (this.wxService.checkSignature(timestamp, nonce, signature)) {
            return echostr;
        }

        return "非法请求";
    }

    @ApiOperation(value = "绑定微信个人信息" )
    @PutMapping("/bind" )
    public ResponseEntity<?> bindUserInfo(@RequestBody Map<String, Object> params) {
        OAuth2Authentication auth = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();
        SecurityUser principal = (SecurityUser) auth.getPrincipal();
        User user = userService.selectByID(principal.getId()).get();
        user.setNickname(params.get("nickName" ).toString());
        user.setGender(Short.parseShort(params.get("gender" ).toString()));
        user.setLanguage(params.get("language" ).toString());
        user.setCity(params.get("city" ).toString());
        user.setProvince(params.get("province" ).toString());
        user.setCountry(params.get("country" ).toString());
        user.setAvatarUrl(params.get("avatarUrl" ).toString());
        userService.modifyById(user);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @PostMapping(produces = "application/xml; charset=UTF-8" )
    public String post(@RequestBody String requestBody,
                       @RequestParam("msg_signature" ) String msgSignature,
                       @RequestParam("encrypt_type" ) String encryptType,
                       @RequestParam("signature" ) String signature,
                       @RequestParam("timestamp" ) String timestamp,
                       @RequestParam("nonce" ) String nonce) {
        final boolean isJson = Objects.equals(this.wxService.getWxMaConfig().getMsgDataFormat(),
                WxMaConstants.MsgDataFormat.JSON);
        if (StringUtils.isBlank(encryptType)) {
            // 明文传输的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromJson(requestBody);
            } else {//xml
                inMessage = WxMaMessage.fromXml(requestBody);
            }

            this.route(inMessage);
            return "success";
        }

        if ("aes".equals(encryptType)) {
            // 是aes加密的消息
            WxMaMessage inMessage;
            if (isJson) {
                inMessage = WxMaMessage.fromEncryptedJson(requestBody, this.wxService.getWxMaConfig());
            } else {//xml
                inMessage = WxMaMessage.fromEncryptedXml(requestBody, this.wxService.getWxMaConfig(),
                        timestamp, nonce, msgSignature);
            }

            this.route(inMessage);
            return "success";
        }

        throw new RuntimeException("invalid encrypt type: " + encryptType);
    }

    private void route(WxMaMessage message) {
        try {
            this.router.route(message);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}
