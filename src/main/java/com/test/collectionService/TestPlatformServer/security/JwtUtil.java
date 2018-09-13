package com.test.collectionService.TestPlatformServer.security;

import com.test.collectionService.TestPlatformServer.services.TestResultCollectionService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;


import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.lang.Math.abs;

/**
 * @Author You Jia
 * @Date 8/6/2018 11:28 AM
 */
@Slf4j
public class JwtUtil {
    public static final long EXPIRATION_TIME = 3600_000; // 1 hour
    public static final String SECRET = "ThisIsAnAutoTestSecret";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String HEADER_STRING = "Authorization";
    public static final String ROLE = "ROLE";
    @Autowired
    public TestResultCollectionService testResultCollectionService;


    public static String generateToken(String userRole) {
        //you can put any data in the map
        HashMap<String, Object> map = new HashMap<>();
        map.put(ROLE, userRole);

        String jwt = Jwts.builder()
                .setClaims(map)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SECRET)
                .compact();
        return TOKEN_PREFIX + " " + jwt;
    }

    public static Map<String, Object> validateTokenAndGetClaims(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token == null)
            throw new TokenValidationException("Missing token");
        // parse the token. exception when token is invalid
        Map<String, Object> body = Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                .getBody();
        return body;
    }

    static class TokenValidationException extends RuntimeException {
        public TokenValidationException(String msg) {
            super(msg);
        }
    }
    public static boolean isValidPassword(Account ac) {
        //we just have 2 hardcoded user
        //use DES to encrypt and decrypt password: admin99%, user99%

        if ("admin".equals(ac.username) && "admin99%".equals(ac.password)
                || "user".equals(ac.username) && "user99%".equals(ac.password) ||"youjia".equals(ac.username) && "youjia99%".equals(ac.password)) {
         long currentTime = new Date().getTime();
            if(abs(currentTime - ac.timestamp) <= 30000){
                return true;
            }

        }
        return false;
    }


    public static class Account {
        public String username;
        public String password;
        public long timestamp;
    }




}
