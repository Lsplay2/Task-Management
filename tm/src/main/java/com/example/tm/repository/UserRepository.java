package com.example.tm.repository;

import com.example.tm.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User getUserById(Long id);
    User getUserByEmail(String email);
    Boolean existsByEmail(String email);
}
