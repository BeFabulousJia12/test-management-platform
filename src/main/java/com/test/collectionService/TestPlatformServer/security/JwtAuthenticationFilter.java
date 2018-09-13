package com.test.collectionService.TestPlatformServer.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import static com.test.collectionService.TestPlatformServer.security.JwtUtil.ROLE;

/**
 * @Author You Jia
 * @Date 8/6/2018 11:27 AM
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final PathMatcher pathMatcher = new AntPathMatcher();
    private String protectUrlPattern = "/db/**";

    public JwtAuthenticationFilter() {
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        try {
            if (isProtectedUrl(request)) {
                log.debug("ProtectedUrl: " + request.getRequestURI() +  " is accessed by -> " + request.getRemoteAddr());
                Map<String, Object> claims = JwtUtil.validateTokenAndGetClaims(request);
                String role = String.valueOf(claims.get(ROLE));
                //最关键的部分就是这里, 我们直接注入了role信息作为权限Authority
                SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                null, null, Arrays.asList(() -> role)));
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
            return;
        }
        filterChain.doFilter(request, response);
    }

    private boolean isProtectedUrl(HttpServletRequest request) {
        return pathMatcher.match(protectUrlPattern, request.getServletPath());
    }

}
