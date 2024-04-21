package kdquiz.users.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import kdquiz.ResponseDto;
import kdquiz.users.dto.*;
import kdquiz.users.jwt.JwtUtil;
import kdquiz.users.service.MailSendService;
import kdquiz.users.service.SignInService;
import kdquiz.users.service.SignUpService;
import kdquiz.users.service.UsersGetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @Autowired
    UsersGetService usersGetService;

    //회원가입
    @Operation(summary = "회원가입", description = "이메일 인증 먼저하고 회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "U001", description = "회원가입 성공"),
            @ApiResponse(responseCode = "U101", description = "이메일 인증 먼저"),
            @ApiResponse(responseCode = "U201", description = "아이디 중복"),
            @ApiResponse(responseCode = "U301", description = "닉넴임 중복")
    })
    @PostMapping("/users/register")
    public ResponseDto<?> SingUp(@RequestBody SignUpDto signUpDto){
        return signUpService.SingUp(signUpDto);
    }

    @Operation(summary = "로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "U003", description = "로그인 성공"),
            @ApiResponse(responseCode = "U103", description = "비밀번호가 일치하지 않음")
    })
    @PostMapping("/users/login")
    public ResponseDto<?> SignIn(@RequestBody SignInDto signInDto, HttpServletResponse response){
//        String userEmail = signInDto.getEmail();
//        String token = jwtUtil.createToken(userEmail);
//        System.out.println("사용자 토큰: "+token);
        return signInService.Signin(signInDto, response);
    }

    @Operation(summary = "이메일 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "U001", description = "이메일 요청 성공"),
            @ApiResponse(responseCode = "U101", description = "이미 가입된 이메일"),
            @ApiResponse(responseCode = "U201", description = "이메일 요청 실패")
    })
    //이메일 요청
    @PostMapping("/mailSend")
    public ResponseDto<?> mailSend(@RequestBody @Valid EmailDto emailDto){
        System.out.println("이메일 인증 이메일: "+emailDto.getEmail());
        return mailSendService.joinEmail(emailDto.getEmail());
    }

    @Operation(summary = "이메일 인증")
    @ApiResponses({
            @ApiResponse(responseCode = "U001", description = "이메일 인증 성공"),
            @ApiResponse(responseCode = "U101", description = "인증 재시도"),
            @ApiResponse(responseCode = "U201", description = "인증번호가 다름"),
            @ApiResponse(responseCode = "U301", description = "그냥 오류"),
    })
    //이메일 인증
    @PostMapping("/mailAuthCheck")
    public ResponseDto<?> AuthCheck(@RequestBody @Valid EmailCheckDto emailCheckDto){
        return mailSendService.CheckAuthNum(emailCheckDto.getEmail(), emailCheckDto.getAuthNum());
    }

    @GetMapping("/users/get")
    public ResponseDto<List<UserGetDto>> usersGet() {
        ResponseDto<List<UserGetDto>> users = (ResponseDto<List<UserGetDto>>) usersGetService.getUsers();
        return new ResponseEntity<>(users, HttpStatus.OK).getBody();
    }
}
