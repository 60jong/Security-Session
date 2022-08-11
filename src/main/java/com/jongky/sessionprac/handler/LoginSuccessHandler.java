package com.jongky.sessionprac.handler;

import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        System.out.println(authentication.getName());
        RequestCache requestCache = new HttpSessionRequestCache();
        SavedRequest savedRequest= requestCache.getRequest(request, response);
        String uri = "/";
        if (savedRequest != null) {
            uri = savedRequest.getRedirectUrl();
            requestCache.removeRequest(request,response);
        }
        response.sendRedirect(uri);
    }
}
