package org.example.repository;

import org.example.entity.File;
import java.util.Optional;

import org.example.entity.Users;
import org.example.exception.UserNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    Optional<Users> findByUserIdEquals(Long id) throws UserNotFoundException;
}