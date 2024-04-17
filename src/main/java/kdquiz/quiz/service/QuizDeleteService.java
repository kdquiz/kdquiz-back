package kdquiz.quiz.service;

import jakarta.transaction.Transactional;
import kdquiz.domain.Quiz;
import kdquiz.ResponseDto;
import kdquiz.domain.Users;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class QuizDeleteService {
    @Autowired
    QuizRepository quizRepository;
    @Transactional
    public ResponseDto<?> QuizDelete(Long quizId, Users users){
        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q304","사용자 못 찾음");
        }
        try{
            Optional<Quiz> quizOptional = quizRepository.findByIdAndEmail(quizId, users.getEmail());
            quizOptional.orElseThrow(()->new IllegalArgumentException("삭제 할 퀴즈를 찾을 수 없습니다."));
            if (quizOptional.isPresent()) {
                quizRepository.delete(quizOptional.get());
                return ResponseDto.setSuccess("Q004", "퀴즈 삭제 성공", null);

            }else{
                return ResponseDto.setFailed("Q104","삭제 할 퀴즈를 찾을 수 없습니다.");

            }
        }catch (Exception e){
            return ResponseDto.setFailed("Q204","퀴즈 삭제 실패");

        }

    }
}
