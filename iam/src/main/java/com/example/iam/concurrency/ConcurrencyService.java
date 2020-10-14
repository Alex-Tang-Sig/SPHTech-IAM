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

  public User getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Object myUser = (auth != null) ? auth.getPrincipal() : null;
    if (myUser instanceof User) {
        User user = (User) myUser;
        return user;
    } else {
        throw new UsernameNotFoundException("当前用户不存在！");
    }
  }

  public List<User> getAllLoggedInUser() {
    final List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
    System.out.println("-----------------------------------");
    System.out.println(allPrincipals.size());
    System.out.println("-----------------------------------");
    List<User> users = new ArrayList<>();
    for(final Object principal : allPrincipals) {
        if(principal instanceof User) {
            final User user = (User) principal;
            users.add(user);            
        }
    }
    return users;
  }

  /*
  public List<SessionInformation> getCurrentSession() {
    getAllSessions(Object principal, boolean includeExpiredSessions) ;
    SessionInformation.expireNow();
  }
  */

  // public void logout(HttpServletRequest request, HttpServletResponse response) {
  //   Authentication auth = SecurityContextHolder.getContext().getAuthentication();
  //   if (auth != null){    
  //     new SecurityContextLogoutHandler().logout(request, response, auth);
  //   }
  // }
}