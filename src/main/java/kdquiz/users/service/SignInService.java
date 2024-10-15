package kdquiz.users.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import kdquiz.ResponseDto;
import kdquiz.users.jwt.JwtUtil;
import kdquiz.domain.Users;
import kdquiz.users.dto.SignInDto;
import kdquiz.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2
public class SignInService {
    private final UsersRepository usersRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public ResponseDto<?> Signin(SignInDto signInDto, HttpServletResponse response){
        String email = signInDto.getEmail();
        String password = signInDto.getPassword();

        //사용자 존재 여부 확인
        Optional<Users> users = usersRepository.findByEmail(email);
        users.orElseThrow(()->new IllegalArgumentException("존재하지 않는 회원 입니다."));

        if(!passwordEncoder.matches(password, users.get().getPassword())){
            return ResponseDto.setFailed("U103","비밀번호가 일치하지 않습니다.");
        }
        System.out.println("userEmail: "+email);
        // 토큰 생성
        String token = jwtUtil.createToken(email);
        String refreshToken = jwtUtil.createRefreshToken(email);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        response.addHeader("Refresh-Token", refreshToken);
        log.info("refreshToekn: "+refreshToken);
        log.info("리스폰: "+response.getHeader("Refresh-Token"));
//        response.addHeader("Access-Control-Allow-Origin", "*");
//        response.addHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
//        response.addHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, X-Requested-With");
         String []list = {token, refreshToken};
        return ResponseDto.setSuccess("U003", "로그인 성공", token);

    }
}
