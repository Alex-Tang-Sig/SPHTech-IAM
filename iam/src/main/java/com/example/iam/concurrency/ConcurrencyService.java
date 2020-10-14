package com.example.iam.concurrency;

import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import com.example.iam.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import java.util.ArrayList;
import java.util.List;


@Service("concurrencyService")
public class ConcurrencyService {

  @Autowired
  @Qualifier("sessionRegistry")
  private SessionRegistry sessionRegistry;


  public void login(User user) {
    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
  }

  public Principal getCurrentPrincipal() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Principal myUser = (auth != null) ? auth.getPrincipal() : null;
    if (myUser instanceof User) {
      return myUser;
    } else {
      throw new UsernameNotFoundException("当前用户不存在！");
    }
  }

  public User getCurrentUser() {
    return (User) getCurrentPrincipal();
  }

  public List<SessionInformation> getUserAllSessions() {
    Principal principal = getCurrentPrincipal();
    final List<SessionInformation> allSessions = sessionRegistry.getAllSessions(principal, false);
    return allSallSessions;
  }

  // public boolean expireNow(SessionInformation sessionInfo) {
  //   return sessionInfo.expireNow();
  // }
}