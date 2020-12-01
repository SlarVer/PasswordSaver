package by.bsu.kas.PasswordSaver.repository;

import by.bsu.kas.PasswordSaver.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

