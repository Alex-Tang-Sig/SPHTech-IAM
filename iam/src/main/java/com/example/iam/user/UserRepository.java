package com.example.iam.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository("userRepository")
public interface UserRepository extends CrudRepository<User, Long> {

  Optional<User> findByUsername(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByUsernameOrEmail(String email, String username);
  
  List<User> findAll();
}