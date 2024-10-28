package com.kojikoji.gpt.domain.security.service;

import com.kojikoji.gpt.domain.security.model.vo.JwtToken;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.codec.Hex;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.PrivilegedAction;

/**
 * @ClassName JwtFilter
 * @Description
 * @Author kojikoji 1310402980@qq.com
 * @Date 2024/10/28 10:27
 * @Version
 */

@Slf4j
public class JwtFilter extends AccessControlFilter {

    /**
     *  判断是否携带有效的 token，返回false，走onAccessDenied流程
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse, Object o) throws Exception {
        return false;
    }

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        JwtToken jwtToken = new JwtToken(request.getParameter("token"));
        try {
            getSubject(servletRequest, servletResponse).login(jwtToken);
            return true;
        } catch (Exception e) {
            log.error("鉴权认证失败", e);
            onLoginFail(servletResponse);
            return false;
        }
    }

    private void onLoginFail(ServletResponse response) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        httpResponse.getWriter().write("Auth Err!");
    }
}
