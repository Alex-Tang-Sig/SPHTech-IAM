package com.example.iam.concurrency;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.session.Session;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import java.util.List;

import com.example.iam.user.User;

@RestController
public class ConcurrencyController {

  private ConcurrencyService concurrencyService;

  @Autowired
  public ConcurrencyController(ConcurrencyService concurrencyService) {
    this.concurrencyService = concurrencyService;
  }
	
  // login
  @PostMapping(value = "/login")
  public ResponseEntity<String> login(User user) {
    // HttpServletRequest request
    // get user login - web security
    // check session limit
    // create session || delete session
    // request.getSession().setAttribute("USERNAME", user.getUsername());
    Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(auth);
    return ResponseEntity.status(HttpStatus.OK).body("success");	
  }

  @GetMapping(value = "/logout")
  public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
      //invalidate the session , this will clear the data from configured database (Mysql/redis/hazelcast)
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null){    
      new SecurityContextLogoutHandler().logout(request, response, auth);
    }
    return ResponseEntity.status(HttpStatus.OK).body("success");	
  }

  // @GetMapping("/session")
  // public ResponseEntity<List<Session>> listSession(HttpServletRequest request) {
  //     //invalidate the session , this will clear the data from configured database (Mysql/redis/hazelcast)
  //     // 从redis 拿到so
  //     List<Session> sessions = concurrencyService.findAll();
  //     return ResponseEntity.status(HttpStatus.OK).body(sessions);
  // }

  @PostMapping("/session/remove")
  public ResponseEntity<String> removeSession(Session session) {
      //invalidate the session , this will clear the data from configured database (Mysql/redis/hazelcast)
      // concurrencyService.findSessionById(session.getId()).invalidate();
      return ResponseEntity.status(HttpStatus.OK).body("success");	
  }
 
}