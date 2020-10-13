package com.example.iam.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

import com.example.iam.model.User;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {

  User findByFirstnameAndLastname(String firstname, String lastname);
  
  List<User> findAll();

}