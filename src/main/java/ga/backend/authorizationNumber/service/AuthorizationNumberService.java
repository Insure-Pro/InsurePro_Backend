package ga.backend.authorizationNumber.service;

import ga.backend.authorizationNumber.entity.AuthorizationNumber;
import ga.backend.authorizationNumber.repository.AuthorizationNumberRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.util.GenerateAuthNum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

@Service
public class AuthorizationNumberService {
    @Value("${spring.mail.username}")
    private String FROM_MAIL;

    private final AuthorizationNumberRepository authorizationNumberRespository;
    private final GenerateAuthNum generateAuthNum;
    private final JavaMailSender mailSender;

    public AuthorizationNumberService(AuthorizationNumberRepository authorizationNumberRespository, GenerateAuthNum generateAuthNum, JavaMailSender mailSender) {
        this.authorizationNumberRespository = authorizationNumberRespository;
        this.generateAuthNum = generateAuthNum;
        this.mailSender = mailSender;
    }

    // CREATE
    public AuthorizationNumber createAuthorizationNumber(AuthorizationNumber authorizationNumber) {
        int authNum = sendEmail(authorizationNumber.getEmail());
        authorizationNumber.setAuthNum(authNum);

        return authorizationNumberRespository.save(authorizationNumber);
    }

    // READ
    public AuthorizationNumber findAuthorizationNumber(String email) {
        AuthorizationNumber authorizationNumber = verifiedAuthorizationNumber(email);
        return authorizationNumber;
    }

    // UPDATE
    public AuthorizationNumber patchAuthorizationNumber(AuthorizationNumber authorizationNumber) {
        AuthorizationNumber findAuthorizationNumber = verifiedAuthorizationNumber(authorizationNumber.getEmail());
        if (authorizationNumber.getAuthNum() != 0) findAuthorizationNumber.setAuthNum(authorizationNumber.getAuthNum());

        return authorizationNumberRespository.save(findAuthorizationNumber);
    }

    // DELETE
    public void deleteAuthorizationNumber(String email) {
        AuthorizationNumber authorizationNumber = verifiedAuthorizationNumber(email);
        authorizationNumberRespository.delete(authorizationNumber);
    }

    // 검증
    public AuthorizationNumber verifiedAuthorizationNumber(String email) {
        Optional<AuthorizationNumber> authorizationNumber = authorizationNumberRespository.findById(email);
        return authorizationNumber.orElseThrow(() -> new BusinessLogicException(ExceptionCode.AUTHORIZATION_NUMBER_NOT_FOUND));
    }

    //이메일 보낼 양식 설정하기
    public int sendEmail(String email) {
        int authNum = generateAuthNum.randomNumberExtend(); // 랜덤 번호
        String title = "InsurePro 회원가입 인증번호 발급"; // 이메일 제목
        String content = // html 형식으로 작성하기
            "<div style=\"font-family: Arial, sans-serif; max-width: 600px; margin: auto; padding: 20px; border: 1px solid #dddddd; border-radius: 10px; background-color: #f9f9f9;\">" +
                "<h2 style=\"color: #333333; text-align: center;\">회원 가입 인증</h2>" +
                "<p style=\"font-size: 16px; color: #666666;\">안녕하세요,</p>" +
                "<p style=\"font-size: 16px; color: #666666;\">회원 가입해 주셔서 감사합니다. 아래는 요청하신 인증 번호입니다:</p>" +
                "<div style=\"text-align: center; margin: 20px 0;\">" +
                "<h1 style=\"color: #7B98FF; font-size: 48px; margin: 0;\">" + authNum + "</h1>" +
                "</div>" +
                    "<p style=\"font-size: 16px; color: #666666;\">해당 인증 번호를 입력란에 입력하여 회원 가입을 완료해 주세요.</p>" +
                    "<p style=\"font-size: 14px; color: #999999; text-align: center;\">만약 이 요청을 하지 않으셨다면, 이 이메일을 무시해 주세요.</p>" +
                    "<div style=\"text-align: center; margin-top: 30px;\">" +
                "</div>" +
            "</div>"; //이메일 내용 삽입
        emailConfig(email, title, content);
        return authNum;
    }

    //이메일 전송 설정 메서드
    public void emailConfig(String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();

        // true 매개값을 전달하면 multipart 형식의 메세지 전달이 가능.문자 인코딩 설정도 가능
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(FROM_MAIL);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content, true); // true 전달 > html 형식으로 전송 , 작성하지 않으면 단순 텍스트로 전달됨
            mailSender.send(message); // 이메일 전송
        } catch (MessagingException e) {
            throw new BusinessLogicException(ExceptionCode.FAIL_SEND_EMAIL);
        }
    }

    // 인증번호 확인
    public AuthorizationNumber checkAuthNum(AuthorizationNumber authorizationNumber) {
        AuthorizationNumber findAuthorizationNumber = verifiedAuthorizationNumber(authorizationNumber.getEmail());
        if (authorizationNumber.getAuthNum() != findAuthorizationNumber.getAuthNum()) // 인증번호가 다를 경우
            throw new BusinessLogicException(ExceptionCode.NOT_MATCH_AUTHNUM);
        return findAuthorizationNumber;
    }

    // 이메일로 인증번호 확인
    public void checkAuthNum(String email, int authNum) {
        AuthorizationNumber authorizationNumber = verifiedAuthorizationNumber(email);
        if (authorizationNumber.getAuthNum() != authNum) // 인증번호가 다를 경우
            throw new BusinessLogicException(ExceptionCode.NOT_MATCH_AUTHNUM);
    }
}
