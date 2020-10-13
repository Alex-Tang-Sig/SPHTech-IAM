package com.example.iam.controller;

import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.HttpStatus;
import java.util.List;

import com.example.iam.model.User;
import com.example.iam.service.UserService;

@RestController
public class UserController {
	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
  private UserService userService;

  @Autowired
  public UserController(BCryptPasswordEncoder bCryptPasswordEncoder, UserService userService) {
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    this.userService = userService;
  }

  // concurrency
  @GetMapping(value="/login")
  public ResponseEntity<String> signUp(User user) {
    // Save user
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    // request session
    userService.saveUser(user);
    return ResponseEntity.status(HttpStatus.OK).body("success");	
  }

  // user management
  @GetMapping(value="/user")
  public ResponseEntity<User> findUserByName(@RequestParam(name="firstname") String firstname, @RequestParam(name="lastname") String lastname) {
    // Save user
    User user = userService.findByName(firstname, lastname);
    // request session
    return ResponseEntity.status(HttpStatus.OK).body(user);	
  }

  @GetMapping(value="/user/list")
  public ResponseEntity<List<User>> getUsers() {
    // Save user
    List<User> users = userService.findAll();
    // request session
    return ResponseEntity.status(HttpStatus.OK).body(users);	
  }

  @PostMapping(value="/user/create")
  public ResponseEntity<String> signUp(User user) {
    // Save user
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    // request session
    userService.saveUser(user);
    return ResponseEntity.status(HttpStatus.OK).body("success");	
  }


}