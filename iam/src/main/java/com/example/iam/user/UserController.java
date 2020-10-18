package com.example.iam.user;

import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
      @RequestParam("username") String username,
      @RequestParam("password") String password,
      @RequestParam("email") String email,
      @RequestParam("browser") String browser,
      HttpServletRequest request,
      HttpSession session) {

    Object sessionBrowser = session.getAttribute("browser");
    if (sessionBrowser == null) {
      System.out.println("不存在 session，设置 browser=" + browser);
      session.setAttribute("browser", browser);
    } else {
      System.out.println("存在 session，browser=" + sessionBrowser.toString());
    }
    Cookie[] cookies = request.getCookies();
    if (cookies != null && cookies.length > 0) {
      for (Cookie cookie : cookies) {
        System.out.println(cookie.getName() + ":" + cookie.getValue());
      }
    }

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
