package com.wind.oauth.integration.other;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import com.wind.common.Constant;
import com.wind.common.SecurityUser;
import com.wind.mybatis.pojo.User;
import com.wind.oauth.integration.IntegrationAuthentication;
import com.wind.oauth.integration.IntegrationAuthenticator;
import com.wind.web.service.UserService;
import lombok.extern.apachecommons.CommonsLog;
import me.chanjar.weixin.common.exception.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@CommonsLog
@Component
public class MiniAppAuthenticator extends IntegrationAuthenticator {

    @Autowired
    private UserService userService;

    @Autowired
    private WxMaService wxMaService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public boolean support(IntegrationAuthentication integrationAuthentication) {
        return "wx_app".equals(integrationAuthentication.getAuthType());
    }

    public SecurityUser authenticate(IntegrationAuthentication integrationAuthentication) {
        WxMaJscode2SessionResult session;
        String code = integrationAuthentication.getAuthParameter("password");
        try {
            session = this.wxMaService.getUserService().getSessionInfo(code);
        } catch (WxErrorException e) {
            e.printStackTrace();
            throw new InternalAuthenticationServiceException("获取微信小程序用户信息失败", e);
        }
        log.info(session);
        String openId = session.getOpenid();
        String sessionKey = session.getSessionKey();
        String unionid = session.getUnionid();
        Optional<User> user = userService.selectByOpenId(openId);
        if (user.isPresent()) {
            User result = user.get();
            result.setPassword(passwordEncoder.encode(code));
            return new SecurityUser(result);
        } else {
            User newUser = new User();
            newUser.setAuthType("wx_app");
            newUser.setOpenId(openId);
            newUser.setSessionKey(sessionKey);
            newUser.setUnionId(unionid);
            newUser.setEnabled(true);
            newUser.setRole(Constant.DEFAULT_ROLE);
            newUser.setPassword(passwordEncoder.encode(code));
            SecurityUser result = new SecurityUser(newUser);
            userService.add(newUser);
            return result;
        }
    }
}
