package skuniv.munchmap.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import skuniv.munchmap.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String email);

    Optional<User> findByLoginId(String loginId);

}
