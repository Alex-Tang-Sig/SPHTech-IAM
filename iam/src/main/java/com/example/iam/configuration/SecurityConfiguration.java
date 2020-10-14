package com.example.iam.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.iam.user.UserService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.core.env.Environment;
@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

  private final UserService userService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;
  
  @Autowired
  private Environment env;

  // @Autowired
  // private SessionRegistry sessionRegistry;

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
      .formLogin()
        .loginProcessingUrl("/login") //the URL on which the clients should post the login information
        .usernameParameter("username") //the username parameter in the queryString, default is 'username'
        .passwordParameter("password") //the password parameter in the queryString, default is 'password'
        .successHandler((httpServletRequest, httpServletResponse, authentication) -> {
          httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        })
        .failureHandler((httpServletRequest, httpServletResponse, authentication) -> {
          httpServletResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        })
      .and()
      .logout()
        .logoutUrl("/logout")
        .permitAll()
        .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
          httpServletResponse.setStatus(HttpServletResponse.SC_OK);
        });

      http
      .sessionManagement()
          .maximumSessions(3)
              .maxSessionsPreventsLogin(true);
  }

  @Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService)
        .passwordEncoder(bCryptPasswordEncoder);
  }
}