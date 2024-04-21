package kdquiz.users.service;

import kdquiz.ResponseDto;
import kdquiz.domain.Users;
import kdquiz.users.dto.UserGetDto;
import kdquiz.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsersGetService {
    @Autowired
    UsersRepository usersRepository;

    @Transactional
    public ResponseDto<List<UserGetDto>> getUsers() {
        List<String> userEmails = usersRepository.findAll()
                .stream()
                .map(user -> user.getEmail())
                .collect(Collectors.toList());

        // UserGetDto로 변환
        List<UserGetDto> userGetDtos = userEmails.stream()
                .map(email -> new UserGetDto(email))
                .collect(Collectors.toList());

        return ResponseDto.setSuccess("000","유저 정보 조회 성공", userGetDtos);
    }
}
