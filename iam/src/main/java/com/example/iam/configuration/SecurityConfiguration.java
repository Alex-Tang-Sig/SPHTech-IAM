package com.example.iam.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.example.iam.user.UserService;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import com.example.iam.user.CustomDaoAuthenticationProvider;
import com.example.iam.user.CustomUserDetailsService;
import com.example.iam.user.EmailPasswordAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
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

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth
      .authenticationProvider(daoAuthenticationProvider());
      // .authenticationProvider(customDaoAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
      http
      .csrf().disable() // for postman api test
      .authorizeRequests()
        .antMatchers("/signup", "/login", "/emaillogin", "/user").permitAll()
        .anyRequest().authenticated()
      .and()
        .formLogin()
      .and()
        .logout()
          .logoutUrl("/logout")
          .permitAll()
          .logoutSuccessHandler((httpServletRequest, httpServletResponse, authentication) -> {
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
          });
      
      // http.addFilterBefore(emailPasswordAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
  }

  @Override
  @Bean
  public AuthenticationManager authenticationManagerBean() throws Exception {
      return super.authenticationManagerBean();
  }

  @Bean
  public DaoAuthenticationProvider daoAuthenticationProvider() {
      DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
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
    return filter;
  }

}