package ga.backend.authorizationNumber.service;

import ga.backend.authorizationNumber.entity.AuthorizationNumber;
import ga.backend.authorizationNumber.repository.AuthorizationNumberRepository;
import ga.backend.exception.BusinessLogicException;
import ga.backend.exception.ExceptionCode;
import ga.backend.util.GenerateAuthNum;
import lombok.AllArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthorizationNumberService {
    private final AuthorizationNumberRepository authorizationNumberRespository;
    private final GenerateAuthNum generateAuthNum;
    private final JavaMailSender mailSender;

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
        if(authorizationNumber.getAuthNum() != 0) findAuthorizationNumber.setAuthNum(authorizationNumber.getAuthNum());

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
        String fromMail = System.getenv("EMAIL_USERNAME"); // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "회원 가입 인증 이메일입니다."; // 이메일 제목
        String content = // html 형식으로 작성하기
                "<h3>" + "요청하신 인증 번호입니다." + "</h3>" +
                        "<br>" +
                        "<h1> " + authNum + "</h1>" +
                        "<br>" +
                        "<h3> 해당 인증번호를 인증번호 확인란에 기입하여 주세요. </h3>"; //이메일 내용 삽입
        emailConfig(fromMail, toMail, title, content);
        return authNum;
    }

    //이메일 전송 설정 메서드
    public void emailConfig(String fromMail, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();

        // true 매개값을 전달하면 multipart 형식의 메세지 전달이 가능.문자 인코딩 설정도 가능
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true,"utf-8");
            helper.setFrom(fromMail);
            helper.setTo(toMail);
            helper.setSubject(title);
            helper.setText(content,true); // true 전달 > html 형식으로 전송 , 작성하지 않으면 단순 텍스트로 전달됨
//            mailSender.send(message); // 이메일 전송
        } catch (MessagingException e) {
            throw new BusinessLogicException(ExceptionCode.FAIL_SEND_EMAIL);
        }
    }

    // 인증번호 확인
    public AuthorizationNumber checkAuthNum(AuthorizationNumber authorizationNumber) {
        AuthorizationNumber findAuthorizationNumber = verifiedAuthorizationNumber(authorizationNumber.getEmail());
        if(authorizationNumber.getAuthNum() != findAuthorizationNumber.getAuthNum()) // 인증번호가 다를 경우
            throw new BusinessLogicException(ExceptionCode.NOT_MATCH_AUTHNUM);
        return findAuthorizationNumber;
    }

    // 이메일로 인증번호 확인
    public void checkAuthNum(String email, int authNum) {
        AuthorizationNumber authorizationNumber = verifiedAuthorizationNumber(email);
        if(authorizationNumber.getAuthNum() != authNum) // 인증번호가 다를 경우
            throw new BusinessLogicException(ExceptionCode.NOT_MATCH_AUTHNUM);
    }
}
