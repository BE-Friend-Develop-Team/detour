package com.befriend.detour.domain.user.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

// 레디스를 통해 인증 번호를 저장하고 조회하며 삭제하는 작업 수행하는 클래스
@RequiredArgsConstructor
@Repository
public class CertificationNumberDao  {

    private final StringRedisTemplate stringRedisTemplate;

    // 이메일을 key로 사용하여 인증 번호를 Redis에 저장
    public void saveCertificationNumber(String email, String certificationNumber) {
        stringRedisTemplate.opsForValue() // opsForValue는 문자열 값을 처리하는 Redis의 기본 작업을 수행
                .set(email, certificationNumber,  // key = email, value = 인증 번호
                        Duration.ofSeconds(180)); // 3분 유효
    }

    // 이메일을 키로 사용하여 Redis에서 인증 번호를 조회
    public String getCertificationNumber(String email) {

        return stringRedisTemplate.opsForValue().get(email);
    }

    // 이메일을 키로 사용하여 Redis에서 인증 번호 삭제
    public void removeCertificationNumber(String email) {
        stringRedisTemplate.delete(email);
    }

    // 이메일을 키로 사용하여 Redis에 해당 키가 존재하는지 확인
    public boolean hasKey(String email) {

        return stringRedisTemplate.hasKey(email);
    }

}