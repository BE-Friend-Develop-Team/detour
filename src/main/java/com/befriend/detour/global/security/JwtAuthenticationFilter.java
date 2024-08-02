package com.befriend.detour.global.security;

import com.befriend.detour.domain.user.dto.LoginRequestDto;
import com.befriend.detour.domain.user.dto.LoginResponseDto;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.entity.UserRoleEnum;
import com.befriend.detour.domain.user.entity.UserStatusEnum;
import com.befriend.detour.domain.user.repository.UserRepository;
import com.befriend.detour.global.dto.CommonResponseDto;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import com.befriend.detour.global.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, UserRepository userRepository) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        if (!request.getMethod().equals("POST")) {
            throw new CustomException(ErrorCode.WRONG_HTTP_REQUEST);
        }

        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(requestDto.getLoginId(), requestDto.getPassword(), null)
            );
        } catch (IOException e) {
            throw new CustomException(ErrorCode.LOGIN_FAIL);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {

        String userId = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getLoginId();
        UserRoleEnum userRole = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();
        UserStatusEnum userStatus = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getStatus();

        Optional<User> userOptional = userRepository.findByLoginId(userId);

        if (userOptional.isEmpty() || userOptional.get().getStatus().equals(UserStatusEnum.WITHDRAWAL) || userOptional.get().getStatus().equals(UserStatusEnum.BLOCK)) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("text/plain;charset=UTF-8");
            response.getWriter().write("유효하지 않은 사용자 정보입니다.");

            return;
        }

        User user = userOptional.get();

        String accessToken = jwtProvider.createAccessToken(userId, userRole);
        String refreshToken = jwtProvider.createRefreshToken(userId);

        user.updateRefresh(refreshToken);
        userRepository.save(user);

        CommonResponseDto commonResponse = new CommonResponseDto(200, "로그인에 성공하였습니다. 🎉", new LoginResponseDto(user));

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(commonResponse));
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        CommonResponseDto commonResponse = new CommonResponseDto(400, "아이디와 비밀번호가 일치하지 않습니다. ⚠", null);
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(commonResponse));
    }

}