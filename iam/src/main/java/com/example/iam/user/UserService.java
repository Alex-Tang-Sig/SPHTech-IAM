package com.example.iam.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Optional;
import java.util.List;

@Service("userService")
public class UserService implements CustomUserDetailsService { // CustomUserDetailsService

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
		return optionalUser.orElseThrow(() -> new UsernameNotFoundException("User cannot be found."));
	}

	@Override
	public User loadUserByEmail(String email) {
		final Optional<User> optionalUser = userRepository.findByEmail(email);
		return optionalUser.orElseThrow(() -> new EmailNotFoundException("User cannot be found."));
	}

	public Boolean signup(User user) {
		boolean isPresent = userRepository.findByUsername(user.getUsername()).isPresent()
				|| userRepository.findByEmail(user.getEmail()).isPresent();
		if (isPresent) {
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
