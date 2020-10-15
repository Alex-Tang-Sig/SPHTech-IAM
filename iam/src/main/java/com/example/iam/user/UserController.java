package com.example.iam.user;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
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
  public ResponseEntity<User> signup(User user) {
    User createdUser = userService.signup(user);
    return ResponseEntity.status(HttpStatus.OK).body(createdUser);	
  }
  
  @GetMapping(value="/user")
  public ResponseEntity<List<User>> findAll() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());	
  }
}