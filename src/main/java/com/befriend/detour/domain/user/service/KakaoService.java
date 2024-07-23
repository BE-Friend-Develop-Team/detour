package com.befriend.detour.domain.user.service;

import com.befriend.detour.domain.user.dto.KaKaoUserInfoDto;
import com.befriend.detour.domain.user.repository.UserRepository;
import com.befriend.detour.global.jwt.JwtProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtProvider jwtProvider;

    @Value("${SOCIAL_KAKAO_CLIENT_ID}")
    private String kakaoClientId;

    @Value("${SOCIAL_KAKAO_REDIRECT_URI}")
    private String kakaoRedirectUri;

    public String kakaoLogin(String code) throws JsonProcessingException {
        // 1. "인가 코드"로 "엑세스 토큰" 요청
        String accessToken = getToken(code);

        // 2. 토큰으로 카카오 API 호출 : "엑세스 토큰"으로 "카카오 사용자 정보"가져오기
        KaKaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        return null;
    }

    private KaKaoUserInfoDto getKakaoUserInfo(String accessToken) {
        return null;
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

        RequestEntity<MultiValueMap<String,String>> requestEntity = RequestEntity
                .post(uri) // POST 메서드로 요청
                .headers(headers) // 헤더 설정
                .body(body); // 바디 설정

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
                requestEntity, // 요청 엔티티
                String.class // 응답 타입
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        // 응답 본문을 JSON 노드로 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        // 액세스 토큰 추출 및 반환
        return jsonNode.get("access_token").asText();
    }

}