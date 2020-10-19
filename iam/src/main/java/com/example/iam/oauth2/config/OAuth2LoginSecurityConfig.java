package com.example.iam.oauth2.config;

import com.example.iam.oauth2.CustomOAuth2UserService;
import com.example.iam.oauth2.HttpCookieOAuth2AuthorizationRequestRepository;
import com.example.iam.oauth2.OAuth2AuthenticationFailureHandler;
import com.example.iam.oauth2.OAuth2AuthenticationSuccessHandler;
import com.example.iam.oauth2.TokenAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired private CustomOAuth2UserService customOAuth2UserService;

  @Autowired private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;
  @Autowired private OAuth2AuthenticationFailureHandler oAuth2AuthenticationFailureHandler;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.oauth2Login()
        .authorizationEndpoint()
        .baseUri("/oauth2/authorize")
        .authorizationRequestRepository(cookieAuthorizationRequestRepository())
        .and()
        .redirectionEndpoint()
        .baseUri("/oauth2/callback/*")
        .and()
        .userInfoEndpoint()
        .userService(customOAuth2UserService)
        .and()
        .successHandler(oAuth2AuthenticationSuccessHandler)
        .failureHandler(oAuth2AuthenticationFailureHandler);
    http.addFilterBefore(tokenAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Bean
  public HttpCookieOAuth2AuthorizationRequestRepository cookieAuthorizationRequestRepository() {
    return new HttpCookieOAuth2AuthorizationRequestRepository();
  }

  @Bean
  public CustomOAuth2UserService customOAuth2UserService() {
    return new CustomOAuth2UserService();
  }

  @Bean
  public TokenAuthenticationFilter tokenAuthenticationFilter() {
    return new TokenAuthenticationFilter();
  }
}
