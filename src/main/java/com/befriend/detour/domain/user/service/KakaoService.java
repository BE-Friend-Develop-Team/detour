package com.befriend.detour.domain.user.service;

import com.befriend.detour.domain.user.dto.KaKaoUserInfoDto;
import com.befriend.detour.domain.user.entity.User;
import com.befriend.detour.domain.user.entity.UserRoleEnum;
import com.befriend.detour.domain.user.entity.UserStatusEnum;
import com.befriend.detour.domain.user.repository.UserRepository;
import com.befriend.detour.global.jwt.JwtProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;

    @Value("${SOCIAL_KAKAO_CLIENT_ID}")
    private String kakaoClientId;

    @Value("${SOCIAL_KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    public String kakaoLogin(String code, HttpServletResponse response) throws JsonProcessingException {
        // 1. "인가 코드"로 "엑세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "엑세스 토큰"으로 "카카오 사용자 정보"가져오기
        KaKaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요 시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. JWT 반환
        String jwtAccessToken = jwtProvider.createAccessToken(kakaoUser.getNickname(), kakaoUser.getRole());
        String jwtRefreshToken = jwtProvider.createRefreshToken(kakaoUser.getNickname());

        kakaoUser.updateRefresh(jwtRefreshToken);
        userRepository.save(kakaoUser);

        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwtAccessToken);
//        response.addHeader("nickname", kakaoUserInfo.getNickname());
        response.setStatus(HttpServletResponse.SC_OK);

        // JWT 토큰을 반환
        return kakaoUser.getNickname();
    }

    private String getToken(String code) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kauth.kakao.com")
                .path("/oauth/token")
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code"); // 인증 타입 설정
        body.add("client_id", kakaoClientId);
        body.add("redirect_uri", kakaoRedirectUri);
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri) // POST 메서드로 요청
                .headers(headers) // 헤더 설정
                .body(body); // 바디 설정

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity, // 요청 엔티티
                String.class // 응답 타입
        );

        log.info(String.valueOf(response));


        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        // 응답 본문을 JSON 노드로 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        // 액세스 토큰 추출 및 반환
        return jsonNode.get("access_token").asText();
    }

    private KaKaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
                .fromUriString("https://kapi.kakao.com") // 카카오 API 서버 URI
                .path("/v2/user/me") // 사용자 정보 요청 경로
                .encode()
                .build()
                .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
                .post(uri)
                .headers(headers)
                .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity,
                String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties")
                .get("nickname").asText();
        String email = jsonNode.get("kakao_account")
                .get("email").asText();

        return new KaKaoUserInfoDto(id, nickname, email);
    }

    // 3. 필요 시에 회원가입
    private User registerKakaoUserIfNeeded(KaKaoUserInfoDto kakaoUserInfo) {
        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {  //DB에 해당 카카오 아이디 없다면 회원가입 진행
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);
            if (sameEmailUser != null) {  //DB에 이미 존재하는 이메일을 가진 회원이 있다면
                kakaoUser = sameEmailUser;  //같은 회원이라고 덮어씌우기
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);  //기존 회원정보에 카카오 Id 추가
            } else {  // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();  //password는 UUID를 사용해서 랜덤으로 생성
                String encodedPassword = passwordEncoder.encode(password);  //암호화

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                kakaoUser = new User(email, encodedPassword, kakaoUserInfo.getNickname(), UserStatusEnum.ACTIVE, UserRoleEnum.USER, kakaoId);
            }
        }

        return kakaoUser;
    }

}