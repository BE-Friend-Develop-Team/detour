package com.befriend.detour.domain.user.repository;

import com.befriend.detour.domain.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositoryCustom {

    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    boolean existsByEmail(String email);

    Optional<User> findByNickname(String nickname);

    Optional<User> findByLoginId(String loginId);

    Optional<User> findByEmail(String Email);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<List<User>> findAllUser(Pageable pageable);

}
