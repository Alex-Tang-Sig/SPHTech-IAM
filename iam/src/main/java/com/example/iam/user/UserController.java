package com.example.iam.user;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import javax.servlet.ServletException;
import java.util.List;


@RestController
public class UserController {
	
  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }
 
  @GetMapping(value="/user")
  public ResponseEntity<List<User>> findAll() {
    List<User> users = userService.findAll();
    return ResponseEntity.status(HttpStatus.OK).body(users);	
  }

  @PostMapping(value="/signup")
  public ResponseEntity<User> signup(HttpServletRequest request, User user) {
    User createdUser = userService.signup(user);
    return ResponseEntity.status(HttpStatus.OK).body(createdUser);	
  }
}