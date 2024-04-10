package kdquiz.users.service;


import kdquiz.ResponseDto;

import kdquiz.users.domain.Users;
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
        String nickname = signUpDto.getNickname();
        Boolean check = false;
        check = mailSendService.SignUpCheck(check);
        if(!check){
            return ResponseDto.setFailed("인증먼저 해주세요");
        }
        Optional<Users> foundEmail = usersRepository.findByEmail(email);
        if(foundEmail.isPresent()){
            return ResponseDto.setFailed("아이디 중복");
        }
        Optional<Users> foundNickname = usersRepository.findByNickname(nickname);
        if(foundNickname.isPresent()){
            return ResponseDto.setFailed("닉넴임 중복");
        }
        password = passwordEncoder.encode(password);
        Users users = new Users();
        users.setEmail(email);
        users.setPassword(password);
        users.setNickname(nickname);
        users.setCreatedAt(LocalDateTime.now());
        usersRepository.save(users);
        return ResponseDto.setSuccess("U001", "회원가입 성공", null);

    }
}