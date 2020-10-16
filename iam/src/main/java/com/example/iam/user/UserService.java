package com.example.iam.user;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userService")
public class UserService implements CustomUserDetailsService {

  private static final String USER_CANNOT_BE_FOUND = "User cannot be found.";
  private UserRepository userRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Autowired
  public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @Override
  public User loadUserByUsername(String username) {
    final Optional<User> optionalUser = userRepository.findByUsername(username);
    return optionalUser.orElseThrow(() -> new UsernameNotFoundException(USER_CANNOT_BE_FOUND));
  }

  @Override
  public User loadUserByEmail(String email) {
    final Optional<User> optionalUser = userRepository.findByEmail(email);
    return optionalUser.orElseThrow(() -> new EmailNotFoundException(USER_CANNOT_BE_FOUND));
  }

  public Boolean signup(User user) {
    final Optional<User> optionalUser =
        userRepository.findByUsernameOrEmail(user.getUsername(), user.getEmail());
    if (optionalUser.isPresent()) {
      return false;
    }
    user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    return true;
  }

  public List<User> findAll() {
    List<User> users = userRepository.findAll();
    users.forEach(user -> user.setPassword(null));
    return users;
  }
}
