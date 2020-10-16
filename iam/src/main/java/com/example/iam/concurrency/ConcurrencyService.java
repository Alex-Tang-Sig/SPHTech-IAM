package com.example.iam.concurrency;

import com.example.iam.user.User;
import java.util.Collection;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.session.FindByIndexNameSessionRepository;
import org.springframework.session.Session;
import org.springframework.stereotype.Service;

@Service("concurrencyService")
public class ConcurrencyService {

  @Autowired FindByIndexNameSessionRepository<? extends Session> sessions;

  public User getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Object principal = (auth != null) ? auth.getPrincipal() : null;
    if (principal instanceof User) {
      User u = (User) principal;
      u.setPassword(null);
      return u;
    } else {
      throw new UsernameNotFoundException("No existing account");
    }
  }

  public Collection<? extends Session> getSession(String username) {
    return sessions.findByPrincipalName(username).values();
  }

  public void removeSession(String username, String sessionIdToDelete) {
    Set<String> usersSessionIds = sessions.findByPrincipalName(username).keySet();
    if (usersSessionIds.contains(sessionIdToDelete)) {
      this.sessions.deleteById(sessionIdToDelete);
    }
  }
}
