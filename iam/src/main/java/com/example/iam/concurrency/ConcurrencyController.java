package com.example.iam.concurrency;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcurrencyController {

  @Autowired ConcurrencyService concurrencyService;

  @GetMapping("/sessions")
  public ResponseEntity<List<SessionInformation>> sessions(Authentication authentication) {
    List<SessionInformation> list = concurrencyService.getAllSessions(authentication);
    return ResponseEntity.status(HttpStatus.OK).body(list);
  }
}
