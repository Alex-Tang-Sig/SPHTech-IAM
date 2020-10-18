package com.example.iam.user;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(value = "/signup")
  public ResponseEntity<String> signup(
      @RequestParam(value = "username", required = false) String username,
      @RequestParam(value = "email", required = false) String email,
      @RequestParam("password") String password,
      HttpServletRequest request) {
    if (username == null && email == null)
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No username or email");
    User user = new User();
    user.setUsername(username);
    user.setPassword(password);
    user.setEmail(email);
    boolean saved = userService.signup(user);
    return saved
        ? ResponseEntity.status(HttpStatus.CREATED).build()
        : ResponseEntity.status(HttpStatus.CONFLICT).build();
  }

  @GetMapping(value = "/user")
  public ResponseEntity<List<User>> findAll() {
    return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
  }
}
