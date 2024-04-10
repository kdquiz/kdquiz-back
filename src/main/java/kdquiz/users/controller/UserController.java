package kdquiz.users.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kdquiz.ResponseDto;
import kdquiz.users.jwt.JwtUtil;
import kdquiz.users.dto.EmailCheckDto;
import kdquiz.users.dto.EmailDto;
import kdquiz.users.dto.SignInDto;
import kdquiz.users.dto.SignUpDto;
import kdquiz.users.service.MailSendService;
import kdquiz.users.service.SignInService;
import kdquiz.users.service.SignUpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    SignUpService signUpService;

    @Autowired
    SignInService signInService;
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    MailSendService mailSendService;

    //회원가입
    @PostMapping("/users/register")
    public ResponseDto<?> SingUp(@RequestBody SignUpDto signUpDto){
        return signUpService.SingUp(signUpDto);
    }

    @PostMapping("/users/login")
    public ResponseDto<?> SignIn(@RequestBody SignInDto signInDto, HttpServletResponse response){
//        String userEmail = signInDto.getEmail();
//        String token = jwtUtil.createToken(userEmail);
//        System.out.println("사용자 토큰: "+token);
        return signInService.Signin(signInDto, response);
    }

    //이메일 컨트롤러
    @PostMapping("/mailSend")
    public ResponseDto<?> mailSend(@RequestBody @Valid EmailDto emailDto){
        System.out.println("이메일 인증 이메일: "+emailDto.getEmail());
        return mailSendService.joinEmail(emailDto.getEmail());
    }

    //이메일 인증 컨트롤러
    @PostMapping("/mailAuthCheck")
    public ResponseDto<?> AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto){
        return mailSendService.CheckAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
    }

}
