package com.example.iam.concurrency;

import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import java.util.ArrayList;
import com.example.iam.user.User;

@Service("concurrencyService")
public class ConcurrencyService {

  public User getCurrentUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    Object myUser = (auth != null) ? auth.getPrincipal() : null;

    if (myUser instanceof User) {
        User user = (User) myUser;
        return user;
    } else {
        log.error("当前用户不存在，请重新登录");
        throw new UsernameNotFoundException("当前用户不存在！");
    }
}
}