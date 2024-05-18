package com.alpergayretoglu.spring_boot_template.repository;

import com.alpergayretoglu.spring_boot_template.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

}
