package com.example.iam.user;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {
  public CustomUserDetails loadUserByEmail(String email);
}
