package com.kojikoji.gpt.domain.security.model.vo;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @ClassName JwtToken
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/10/26 10:59
 * @Version
 */

public class JwtToken implements AuthenticationToken {
    private String jwt;

    public JwtToken(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public Object getPrincipal() {
        return jwt;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }
}
