package com.example.iam.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;


@Service("userService")
public class UserService implements UserDetailsService {

	private UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	
  @Autowired
  public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) { 
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public UserDetails loadUserByUsername(String username) {
		final Optional<User> optionalUser = userRepository.findByUsername(username);
		return optionalUser.orElseThrow(() -> new UsernameNotFoundException("User cannot be found."));
  }
	
	public User signup(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		User u = new User();
		u.setUsername(user.getUsername());
		return u;
	}

	public List<User> findAll() {
		List<User> users = userRepository.findAll();
		users.forEach(user -> user.setPassword(null));
		return users;
	}
	
}