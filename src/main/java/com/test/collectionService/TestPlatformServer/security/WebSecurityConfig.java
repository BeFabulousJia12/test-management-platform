package com.test.collectionService.TestPlatformServer.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


/**
 * @Author You Jia
 * @Date 8/6/2018 11:23 AM
 */
@EnableWebSecurity

public class WebSecurityConfig{

    @Configuration
    @Order(1)
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class DBApiSecurityConfigure extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().authorizeRequests()
                    .antMatchers("/db/**").not().hasAuthority("admin")
                    //antMatchers(HttpMethod.POST, "/login").permitAll()
                    .anyRequest().permitAll()
                    .and()
                    // We filter the db/** requests
                    .addFilterBefore(new JwtAuthenticationFilter(),
                            UsernamePasswordAuthenticationFilter.class);
        }
    }
    @Configuration
    public static class FormLoginSecurityConfigure extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().authorizeRequests()
                    .anyRequest().authenticated().and().formLogin();
        }
    }
    @Configuration
    public class MyWebAppConfigurer extends WebMvcConfigurationSupport {

        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/api/**")
                    .allowedOrigins("*")
                    .allowedMethods("GET", "POST")
                    .allowCredentials(true).maxAge(3600);
        }

    }
}
