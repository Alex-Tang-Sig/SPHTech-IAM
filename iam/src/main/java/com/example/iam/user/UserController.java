package com.example.iam.user;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
public class UserController {
	
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(value="/signup")
  public ResponseEntity<String> signup(User user) {
    boolean saved = userService.signup(user);
    return saved ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.status(HttpStatus.CONFLICT).build();
  }
  
  @GetMapping(value="/user")
  public ResponseEntity<List<User>> findAll() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());	
  }
}