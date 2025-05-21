package net.thanhdevjava.to_do_list.repository;

import net.thanhdevjava.to_do_list.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    // Check if a username already exists in the database
    boolean existsByUsername(String username);
}
