package com.example.iam.concurrency;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConcurrencyController {

  @RequestMapping("/test/cookie")
  public String cookie(
      @RequestParam("browser") String browser, HttpServletRequest request, HttpSession session) {
    // 取出 session 中的 browser
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
    return "index";
  }
}
