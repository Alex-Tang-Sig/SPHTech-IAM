package com.example.iam.concurrency;

import com.example.iam.user.User;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcurrencyController {

  private ConcurrencyService concurrencyService;

  @Autowired
  public ConcurrencyController(ConcurrencyService concurrencyService) {
    this.concurrencyService = concurrencyService;
  }

  @GetMapping("/session")
  public ResponseEntity<User> getCurrentUser(HttpServletRequest httpServletRequest) {
    return ResponseEntity.status(HttpStatus.OK).body(concurrencyService.getCurrentUser());
  }

  @GetMapping("/sessions")
  public ResponseEntity<Object> getSessions(String username) {
    return ResponseEntity.status(HttpStatus.OK).body(concurrencyService.getSession(username));
  }

  @PostMapping("/sessions/{sessionIdToDelete}")
  public ResponseEntity<String> removeSession(
      String username, @PathVariable String sessionIdToDelete) {
    concurrencyService.removeSession(username, sessionIdToDelete);
    return ResponseEntity.status(HttpStatus.OK).body("success");
  }
}
