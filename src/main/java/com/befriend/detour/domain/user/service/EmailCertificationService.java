package com.befriend.detour.domain.user.service;

import com.befriend.detour.domain.user.dao.CertificationNumberDao;
import com.befriend.detour.global.exception.CustomException;
import com.befriend.detour.global.exception.ErrorCode;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@RequiredArgsConstructor
@Service
public class EmailCertificationService {

    // 이메일 전송을 위한 Spring의 메일 전송 인터페이스
    private final JavaMailSender mailSender;
    private final CertificationNumberDao certificationNumberDao;

    public void sendEmailForCertification(String email) throws NoSuchAlgorithmException, MessagingException {
        // 인증번호 생성
        String certificationNumber = getCertificationNumber();
        // 아래 링크를 클릭하여 인증을 완료하도록
        // TODO: 도메인 구매시 링크 변경 필요
        String content = String.format("%s/api/users/verify?certificationNumber=%s&email=%s   링크를 3분 이내에 클릭해주세요.", "http://localhost:8081", certificationNumber, email);

        sendMail(email, content);
        certificationNumberDao.saveCertificationNumber(email, certificationNumber);
    }

    private static String getCertificationNumber() throws NoSuchAlgorithmException {
        String result;

        do {
            int i = SecureRandom.getInstanceStrong().nextInt(999999);
            result = String.valueOf(i);
        } while (result.length() != 6);

        return result;
    }

    private void sendMail(String email, String content) throws MessagingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage(); // 새로운 MIME 메시지를 생성
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage); // 메시지의 다양한 속성을 설정하는 헬퍼

        helper.setTo(email); // 수신자 이메일 주소 설정
        helper.setSubject("detour 사이트 인증 요청 메일입니다."); // 제목 설정
        helper.setText(content); // 이메일 본문 설정
        mailSender.send(mimeMessage); // 이메일 전송
    }

    public void verifyEmail(String certificationNumber, String email) {
        if (isVerify(certificationNumber, email)) {
            throw new CustomException(ErrorCode.VERIFY_NOT_ALLOWED);
        }
        certificationNumberDao.removeCertificationNumber(email); // 유효성 인증 검사 성공시 레디스에서 인증번호 삭제
    }

    private boolean isVerify(String certificationNumber, String email) {
        return !(certificationNumberDao.hasKey(email) && // 인증번호가 저장되어 있는지 확인
                certificationNumberDao.getCertificationNumber(email) // 인증번호 들고옴
                        .equals(certificationNumber)); // 인증번호와 제공된 인증번호 비교
    }

}