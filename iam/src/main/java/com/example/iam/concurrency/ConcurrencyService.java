package com.example.iam.concurrency;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;

@Service("concurrencyService")
public class ConcurrencyService {

  @Autowired SessionRegistry sessionRegistry;

  public List<SessionInformation> getAllSessions(Authentication authentication) {
    Object principal1 = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    return sessionRegistry.getAllSessions(authentication.getPrincipal(), false);
  }
}
