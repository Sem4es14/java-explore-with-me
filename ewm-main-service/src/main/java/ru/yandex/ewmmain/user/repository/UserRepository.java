package ru.yandex.ewmmain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.ewmmain.user.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

}
