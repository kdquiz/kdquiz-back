package kdquiz.quiz.service;

import kdquiz.ResponseDto;
import kdquiz.domain.Quiz;
import kdquiz.domain.Users;
import kdquiz.quiz.dto.Quiz.QuizUpdateDto;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.OptionRepository;
import kdquiz.quiz.repository.QuestionRepository;
import kdquiz.quiz.repository.QuizRepository;
import kdquiz.users.repository.UsersRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class QuizUpdateService {

    private final QuizRepository quizRepository;

    private final UsersRepository usersRepository;

    public QuizUpdateService(QuizRepository quizRepository, UsersRepository usersRepository) {
        this.quizRepository = quizRepository;
        this.usersRepository = usersRepository;
    }

    @Transactional
    public ResponseDto<Void> QuizUpdate(Long quizId, QuizUpdateDto quizUpdateeDto, Users users) {

        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try {
            // 기존 퀴즈를 찾음
            Optional<Quiz> quizOptional = quizRepository.findByIdAndEmail(quizId, users.getEmail());
            quizOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));
            Optional<Users> user = usersRepository.findByEmail(users.getEmail());
            user.orElseThrow(()->new IllegalArgumentException("계정을 찾을 수 없습니다."));

            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = quizOptional.get();
            if(!quizUpdateeDto.getTitle().isEmpty()){
                quiz.setTitle(quizUpdateeDto.getTitle());
            }
            quiz.setUpdatedAt(LocalDateTime.now());
            quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음


            return ResponseDto.setSuccess("Q003", "퀴즈 업데이트 성공", null);
//                    ResponseDto.<Void>builder()
//                    .code("Q003")
//                    .status(200)
//                    .message("퀴즈 업데이트 성공")
//                    .data(null)
//                    .build();
        } catch (Exception e) {
            return ResponseDto.setFailed("Q203","퀴즈 업데이트 실패");
//                    ResponseDto.<Void>builder()
//                    .code("Q103")
//                    .status(500)
//                    .message("퀴즈 업데이트 실패")
//                    .data(null)Øø
//                    .build();
        }
    }
}
