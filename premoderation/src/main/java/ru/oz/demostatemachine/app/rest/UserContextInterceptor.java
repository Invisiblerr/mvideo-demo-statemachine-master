package ru.oz.demostatemachine.app.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * UserContextInterceptor.
 *
 * @author Igor_Ozol
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class UserContextInterceptor implements HandlerInterceptor {

    private final UserContext userContext;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        String userName = request.getHeader("X-User-Name");
        userContext.setUserName(userName);
        return true;
    }

    @PostConstruct
    public void info() {
        log.info("UserContextInterceptor was loaded!");
    }
}
