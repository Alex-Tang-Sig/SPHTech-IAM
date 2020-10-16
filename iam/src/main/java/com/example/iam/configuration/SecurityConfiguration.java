package com.example.iam.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.example.iam.user.UserService;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.context.annotation.Bean;
import com.example.iam.user.CustomDaoAuthenticationProvider;
import com.example.iam.user.EmailPasswordAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationDetailsSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserService userService;

  @Autowired
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  @Qualifier("authenticationManagerBean")
  private AuthenticationManager authenticationManager;

  @Autowired
  private SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler;

  @Autowired
  private AuthenticationFailureHandler authenticationFailureHandler;

  @Autowired
  private AuthenticationEntryPoint authenticationEntryPoint;

  @Autowired
  private AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource;

  @Bean
  public AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails> authenticationDetailsSource() {
    return new AuthenticationDetailsSource<HttpServletRequest, WebAuthenticationDetails>() {
      @Override
      public WebAuthenticationDetails buildDetails(HttpServletRequest request) {
        return new WebAuthenticationDetails(request);
      }
    };
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(customDaoAuthenticationProvider());
    auth.authenticationProvider(daoAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable() // for postman api test
        .authorizeRequests().antMatchers("/signup", "/login", "/login/email", "/user").permitAll().anyRequest()
        .authenticated();

    http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint());

    http.logout().permitAll().invalidateHttpSession(true).deleteCookies("JSESSIONID")
        .logoutSuccessHandler(logoutSuccessHandler());

    http.addFilterBefore(usernamePasswordAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class);
    http.addFilterBefore(emailPasswordAuthenticationFilter(), AbstractPreAuthenticatedProcessingFilter.class);
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
    daoAuthenticationProvider.setUserDetailsService(userService);
    daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
    return daoAuthenticationProvider;
  }

  @Bean
  public CustomDaoAuthenticationProvider customDaoAuthenticationProvider() {
    CustomDaoAuthenticationProvider customDaoAuthenticationProvider = new CustomDaoAuthenticationProvider();
    customDaoAuthenticationProvider.setUserDetailsService(userService);
    customDaoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
    return customDaoAuthenticationProvider;
  }

  @Bean
  public EmailPasswordAuthenticationFilter emailPasswordAuthenticationFilter() {
    EmailPasswordAuthenticationFilter filter = new EmailPasswordAuthenticationFilter();
    filter.setAuthenticationManager(authenticationManager);
    filter.setAuthenticationDetailsSource(authenticationDetailsSource);
    filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    filter.setAuthenticationFailureHandler(authenticationFailureHandler);
    filter.setFilterProcessesUrl("/login/email");
    return filter;
  }

  @Bean
  public UsernamePasswordAuthenticationFilter usernamePasswordAuthenticationFilter() {
    UsernamePasswordAuthenticationFilter filter = new UsernamePasswordAuthenticationFilter();
    filter.setAuthenticationManager(authenticationManager);
    filter.setAuthenticationDetailsSource(authenticationDetailsSource);
    filter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
    filter.setAuthenticationFailureHandler(authenticationFailureHandler);
    filter.setFilterProcessesUrl("/login");
    return filter;
  }

  @Bean
  public LogoutSuccessHandler logoutSuccessHandler() { // 登出处理
    return new LogoutSuccessHandler() {

      @Override
      public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
          Authentication authentication) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_OK);
      }
    };
  }

  @Bean
  public SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler() {
    return new SavedRequestAwareAuthenticationSuccessHandler() {

      @Override
      public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
          Authentication authentication) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
          session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
        response.setStatus(HttpServletResponse.SC_OK);
      }
    };
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new SimpleUrlAuthenticationFailureHandler() {

      @Override
      public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
          AuthenticationException exception) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, exception.getMessage());
      }
    };
  }

  @Bean
  public AuthenticationEntryPoint authenticationEntryPoint() {
    return new CustomAuthenticationEntryPoint();
  }

  private class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException authException) throws IOException, ServletException {
      response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
  }
}
