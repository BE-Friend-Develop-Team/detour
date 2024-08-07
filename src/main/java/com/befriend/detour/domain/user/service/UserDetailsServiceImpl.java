package com.befriend.detour.domain.user.service;

import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.entity.UserStatusEnum;
import com.befriend.detour.domain.user.repository.UserRepository;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import com.befriend.detour.global.security.UserDetailsImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        Optional<User> userOptional = userRepository.findByNickname(loginId);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다: " + loginId);
        }

        User user = userOptional.get();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        if(user.getStatus().equals(UserStatusEnum.BLOCK))
        {
            throw new CustomException(ErrorCode.USER_NOT_ACTIVE);
        }

        return userDetails;

    }

}