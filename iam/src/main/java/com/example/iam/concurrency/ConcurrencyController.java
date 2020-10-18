package com.example.iam.concurrency;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcurrencyController {

  @Autowired ConcurrencyService concurrencyService;

  @GetMapping("/sessions")
  public ResponseEntity<List<SessionInformation>> getAllSessions(Authentication authentication) {
    List<SessionInformation> list = concurrencyService.getAllSessions(authentication);
    return ResponseEntity.status(HttpStatus.OK).body(list);
  }

  @PostMapping("/sessions/{sessionId}")
  public ResponseEntity<String> expireSession(
      Authentication authentication, @PathVariable String sessionId) {
    boolean isExpired = concurrencyService.expireNow(authentication, sessionId);
    return isExpired
        ? ResponseEntity.status(HttpStatus.OK).build()
        : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
  }
}
