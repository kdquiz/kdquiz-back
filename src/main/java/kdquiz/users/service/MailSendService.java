package kdquiz.users.service;

import kdquiz.ResponseDto;
import kdquiz.domain.EmailCheck;
import kdquiz.domain.Users;
import kdquiz.users.repository.EmailCheckRepository;
import kdquiz.users.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;
@Component
@Service
public class MailSendService {
    @Autowired
    private JavaMailSender mailSender;
    private int authNumber;

    @Value("${spring.mail.username}")
    private String EmailId;

    @Autowired
    private EmailCheckRepository emailCheckRepository;

    @Autowired
    private UsersRepository usersRepository;

    private Boolean authemail = false;



    //임의의 6자리 양수를 반환합니다.
    public void makeRandomNumber(){
        Random r = new Random();
        String randomNumber = "";
        for(int i=0; i<6; i++){
            randomNumber += Integer.toString(r.nextInt(10));
        }
        authNumber = Integer.parseInt(randomNumber);
    }

    //mail을 어디서 보내는지, 어디로 보내는지, 인증 번호를 html 형식으로 어떻게 보내는지 작성
    public ResponseDto<?> joinEmail(String email){
        Optional<Users> users = usersRepository.findByEmail(email);
        if(users.isPresent()){
            return ResponseDto.setFailed("U101","이미 가입된 이메일 입니다.");
        }

        makeRandomNumber();
        String setFrom = EmailId; // email-config에 설정한 자신의 이메일 주소를 입력
        String toMail = email;
        String title = "키득Quiz 회원가입 인증 이메일 입니다."; //이메일 제목
        String content =
                "<center style=\"width: 100%\">"+
                    "<center style=\"      width: 700px;      height: 480px;      border-radius: 12px;      background-color: #85aeff;      margin: 12px;      padding: 12px;      display: flex;      flex-direction: column;      box-shadow: 0 4px 4px 0 rgba(51, 51, 51, 0.5);    \">"+
                        "<div style=\"display: flex; width: 100%; justify-content: space-between\">"+
                            "<div style=\"          width: 12px;          height: 12px;          border-radius: 100%;          background-color: #faec71;          box-shadow: 0px 4px 4px 0 rgba(51, 51, 51, 0.5);        \"></div>"+
                            "<div style=\"          width: 12px;          height: 12px;          border-radius: 100%;          background-color: #faec71;          box-shadow: 0px 4px 4px 0 rgba(51, 51, 51, 0.5);        \"></div>"+
                        "</div>"+
                        "<div style=\"        display: flex;        width: 100%;        height: 100%;        padding-left: 12px;        padding-right: 12px;      \">"+
                            "<center style=\"          box-shadow: 0px 2px 4px 0 rgba(51, 51, 51, 0.5);          display: flex;          width: calc(100% - 24px);          align-items: center;          justify-content: center;          border-radius: 12px;          background-color: white;          flex-direction: column;          gap: 12px;        \">"+
                                "<xtext style=\"font-size: 36px; font-weight: 700; color: #646363\">"+
                                "키득Quiz 가입을 환영합니다."+
                                "</xtext>"+
                                    "<xtext style=\"font-size: 36px; font-weight: 700; color: #646363\">"+
                                        "인증코드"+authNumber+
                                        "</xtext>"+
                                "<xtext style=\"font-size: 72px; font-weight: 700; color: #646363\">"+
                                "</xtext>"+
                            "</center>"+
                        "</div>"+
                        "<div style=\"display: flex; width: 100%; justify-content: space-between\">"+
                            "<div style=\"          width: 12px;          height: 12px;          border-radius: 100%;          box-shadow: 0px 4px 4px 0 rgba(51, 51, 51, 0.5);          background-color: #faec71;        \"></div>"+
                            "<div style=\"          width: 12px;          height: 12px;          border-radius: 100%;          box-shadow: 0px 4px 4px 0 rgba(51, 51, 51, 0.5);          background-color: #faec71;        \"></div>"+
                        "</div>"+
                    "</center>"+
                "</center>";

//                "키득Quiz 회원가입을 환영합니다."+
//                        "<br><br>"+
//                        "인증번호는[ <span><h1>"+authNumber+"</h1></span> ]입니다.";

        mailSender(setFrom, toMail, title, content);
        return ResponseDto.setSuccess("U001", "이메일 요청 성공", authNumber );
    }

    //이메일 전송
    private ResponseDto<?> mailSender(String setFrom, String toMail, String title, String content) {
        MimeMessage message = mailSender.createMimeMessage();//JavaMailSender 객체를 사용하여 MimeMessage 객체를 생성
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8"); //이메일 메시지와 관련된 설정을 수행합니다.
            // true를 전달하여 multipart 형식의 메시지를 지원하고, "utf-8"을 전달하여 문자 인코딩을 설정
            helper.setFrom(setFrom);//이메일의 발신자 주소 설정
            helper.setTo(toMail);//이메일의 수신자 주소 설정
            helper.setSubject(title);//이메일의 제목을 설정
            helper.setText(content, true);//이메일의 내용 설정 두 번째 매개 변수에 true를 설정하여 html 설정으로한다.
            mailSender.send(message);
        } catch (MessagingException e) {//이메일 서버에 연결할 수 없거나, 잘못된 이메일 주소를 사용하거나, 인증 오류가 발생하는 등 오류
                // 이러한 경우 MessagingException이 발생
                e.printStackTrace();//e.printStackTrace()는 예외를 기본 오류 스트림에 출력하는 메서드
            return ResponseDto.setFailed("U201", "이메일 전송 실패");
            }
        EmailCheck emailCheck = new EmailCheck();
        emailCheck.setEmail(toMail);
        emailCheck.setAuth(Integer.toString(authNumber));
        emailCheck.setCreatedAt(LocalDateTime.now());
        emailCheckRepository.save(emailCheck);
        return ResponseDto.setSuccess("U001", "이메일 전송 성공", null);
    }

    //이메일 인증
    public ResponseDto<?> CheckAuthNum(String email, String authNum){
        Optional<EmailCheck> emailCheckOpt = emailCheckRepository.findByEmailAndAuth(email, authNum);

        if (emailCheckOpt.isPresent()) {
            EmailCheck emailCheck = emailCheckOpt.get();

            // 인증번호 확인
            emailCheckRepository.delete(emailCheck);

            // 인증 성공 처리
            authemail = true;
            return ResponseDto.setSuccess("U001", "이메일 인증이 되었습니다.", null);
        } else {
            // 인증 실패 처리
            return ResponseDto.setFailed("U201", "인증번호가 다릅니다.");
        }
    }

    public Boolean SignUpCheck(Boolean check) {
        if (authemail) {
            authemail = false;
            return true;
        }else {
            return false;
        }
    }

//    @Scheduled(fixedRate = 60000) // 5분마다 실행
//    @Transactional
//    public void deleteOldData() {
//        LocalDateTime now = LocalDateTime.now();
//        LocalDateTime timeThreshold = now.minusMinutes(5);
//        System.out.println("삭제 실행");
//        emailCheckRepository.deleteByCreatedAtBefore(timeThreshold);
//        LocalDateTime fiveMinutesAgo = LocalDateTime.now().minus(5, ChronoUnit.MINUTES); //생성된 지 5분된 데이터 삭제
//        System.out.println("삭제 실행");
//        emailCheckRepository.deleteByCreatedAtBefore(fiveMinutesAgo);
//    }

}
