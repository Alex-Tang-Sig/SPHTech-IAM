package com.example.iam.user;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface CustomUserDetailsService extends UserDetailsService {
	// ~ Methods
	// ========================================================================================================
  CustomUserDetails loadUserByEmail(String email);
}
