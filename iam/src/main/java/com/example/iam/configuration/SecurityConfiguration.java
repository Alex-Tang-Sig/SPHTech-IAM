package com.example.iam.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.iam.user.UserService;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.session.SessionRegistry;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final UserService userService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  
  @Autowired
  private Environment env;

  @Autowired
  public SecurityConfiguration(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userService = userService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http
      .csrf().disable() // for postman api test
      .authorizeRequests()
        .antMatchers("/signup", "/login").permitAll()
        .anyRequest().authenticated()
      .and()
      .logout()
        .logoutUrl("/logout")
        .permitAll()
        .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
          httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        });
      
      http.sessionManagement()
        .maximumSessions(3) //Integer.parseInt(env.getProperty("spring.maxuser.sessions"))
        .maxSessionsPreventsLogin(true)
        .sessionRegistry(sessionRegistry())
        .expiredUrl("/");
  }

  @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
        .passwordEncoder(bCryptPasswordEncoder);
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }

  @Bean
  public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
    return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
  }
}



