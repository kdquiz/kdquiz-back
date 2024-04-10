package kdquiz.users.service;

import jakarta.servlet.http.HttpServletResponse;

import kdquiz.ResponseDto;
import kdquiz.users.jwt.JwtUtil;
import kdquiz.users.domain.Users;
import kdquiz.users.dto.SignInDto;
import kdquiz.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
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
            return ResponseDto.setFailed("비밀번호가 일치하지 않습니다.");
        }
        System.out.println("userEmail: "+email);
        String token = jwtUtil.createToken(email);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(email));
        return ResponseDto.setSuccess("U002", "로그인 성공", token );

    }
}
