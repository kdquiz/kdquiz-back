package kdquiz.quiz.service;

import jakarta.transaction.Transactional;
import kdquiz.quiz.domain.Quiz;
import kdquiz.quiz.exception.ResponseDto;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class QuizDeleteService {
    @Autowired
    QuizRepository quizRepository;
    @Transactional
    public ResponseDto<?> QuizDelete(Long quizId){
        try{
            Optional<Quiz> quizOptional = quizRepository.findById(quizId);
            if (quizOptional.isPresent()) {
                quizRepository.delete(quizOptional.get());
                return ResponseDto.<Void>builder()
                        .code("Q004")
                        .status(200)
                        .message("퀴즈 삭제 성공")
                        .data(null)
                        .build();
            }else{
                return ResponseDto.<Void>builder()
                        .code("Q404")
                        .status(404)
                        .message("삭제할 퀴즈를 찾을 수 없습니다.")
                        .data(null)
                        .build();
            }

        }catch (Exception e){
            return ResponseDto.<Void>builder()
                    .code("Q504")
                    .status(500)
                    .message("퀴즈 삭제 실패")
                    .data(null)
                    .build();
        }

    }
}
