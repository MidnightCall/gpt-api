package com.kojikoji.gpt.domain.security.service.realm;

import com.kojikoji.gpt.domain.security.model.vo.JwtToken;
import com.kojikoji.gpt.domain.security.service.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Objects;

/**
 * @ClassName JwtRealm
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/10/27 21:45
 * @Version
 */

@Slf4j
public class JwtRealm extends AuthorizingRealm {

    private static JwtUtil jwtUtil = new JwtUtil();

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String jwt = (String) token.getPrincipal();
        if (Objects.isNull(jwt)) {
            throw new NullPointerException("jwtToken 不允许为空");
        }
        if (!jwtUtil.isVerify(jwt)) {
            throw new UnknownAccountException();
        }
        String username = (String) jwtUtil.decode(jwt).get("username");
        log.info("鉴权用户 username: {}", username);
        return new SimpleAuthenticationInfo(jwt, jwt, "JwtRealm");
    }
}
