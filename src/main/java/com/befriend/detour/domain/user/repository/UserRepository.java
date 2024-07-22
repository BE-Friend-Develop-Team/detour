package com.befriend.detour.domain.user.repository;

import com.befriend.detour.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNickname(String nickname);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(String Email);

}
