package com.example.iam.concurrency;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;
import org.springframework.session.Session;
import java.util.List;

import com.example.iam.user.User;

@RestController
public class ConcurrencyController {

  private ConcurrencyService concurrencyService;

  @Autowired
  public ConcurrencyController(ConcurrencyService concurrencyService) {
    this.concurrencyService = concurrencyService;
  }
	
  @PostMapping(value = "/login")
  public ResponseEntity<String> login(User user) {
    concurrencyService.login(user);
    return ResponseEntity.status(HttpStatus.OK).body("success");	
  }


  @GetMapping(value = "/active")
  public ResponseEntity<User> getUser() {
    User 
    return ResponseEntity.status(HttpStatus.OK).body();
  }

  @GetMapping(value = "/concurrency")
  public ResponseEntity<List<User>> getUserAllSessions() {
    List<SessionInformation> sessions = concurrencyService.getUserAllSessions();
    return ResponseEntity.status(HttpStatus.OK).body(sessions);
  }

  // @GetMapping(value = "/concurrency/remove")
  // public ResponseEntity<List<User>> getUserAllSessions() {
  //   List<SessionInformation> sessions = concurrencyService.getUserAllSessions();
  //   return ResponseEntity.status(HttpStatus.OK).body(sessions);
  // }

}