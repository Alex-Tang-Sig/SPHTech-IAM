package com.example.iam.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.iam.model.User;
import com.example.iam.repository.UserRepository;
import java.util.List;


@Service("userService")
public class UserService {

	private UserRepository userRepository;
	
  @Autowired
  public UserService(UserRepository userRepository) { 
    this.userRepository = userRepository;
  }
    
	public User findByName(String firstName, String lastName) {
		return userRepository.findByFirstnameAndLastname(firstName, lastName);
  }
  
  public List<User> findAll() {
		return userRepository.findAll();
	}
	
	public void saveUser(User user) {
		userRepository.save(user);
	}

}