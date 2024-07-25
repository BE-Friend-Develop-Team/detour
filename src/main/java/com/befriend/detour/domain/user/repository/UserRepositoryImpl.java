package com.befriend.detour.domain.user.repository;

import com.befriend.detour.domain.user.entity.QUser;
import com.befriend.detour.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.befriend.detour.domain.user.entity.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<User> findByNickname(String nickname) {

        User result =  jpaQueryFactory.selectFrom(user)
                .where(user.nickname.eq(nickname))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findByLoginId(String loginId) {

        User result = jpaQueryFactory.selectFrom(user)
                .where(user.loginId.eq(loginId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findByEmail(String email) {

        User result = jpaQueryFactory.selectFrom(user)
                .where(user.email.eq(email))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<User> findByKakaoId(Long kakaoId) {

        User result = jpaQueryFactory.selectFrom(user)
                .where(user.kakaoId.eq(kakaoId))
                .fetchOne();

        return Optional.ofNullable(result);
    }

    @Override
    public Optional<List<User>> findAllUser(Pageable pageable) {
        QUser qUser = user;

        List<User> users = jpaQueryFactory
                .selectFrom(user)
                .orderBy(qUser.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return Optional.ofNullable(users);
    }

}
