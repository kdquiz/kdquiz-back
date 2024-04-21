package kdquiz.users.service;


import kdquiz.ResponseDto;

import kdquiz.domain.Users;
import kdquiz.users.dto.SignUpDto;
import kdquiz.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    MailSendService mailSendService;

    @Transactional
    public ResponseDto<?> SingUp(SignUpDto signUpDto){
        String email = signUpDto.getEmail();
        String password = signUpDto.getPassword();
        Boolean check = false;
        check = mailSendService.SignUpCheck(check);
        if(!check){
            return ResponseDto.setFailed("U002", "인증먼저 해주세요");
        }
        Optional<Users> foundEmail = usersRepository.findByEmail(email);
        if(foundEmail.isPresent()){
            return ResponseDto.setFailed("U102", "아이디 중복");
        }
        password = passwordEncoder.encode(password);
        Users users = new Users();
        users.setEmail(email);
        users.setPassword(password);
        users.setCreatedAt(LocalDateTime.now());
        usersRepository.save(users);
        return ResponseDto.setSuccess("U002", "회원가입 성공", null);

    }
}
