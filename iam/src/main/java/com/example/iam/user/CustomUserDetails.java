package com.example.iam.user;

import org.springframework.security.core.userdetails.UserDetails;

/**
 * CustomUserDetails
 */
public interface CustomUserDetails extends UserDetails {

  String getEmail();
  
}