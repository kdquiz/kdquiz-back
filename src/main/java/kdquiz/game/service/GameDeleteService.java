package kdquiz.game.service;

import jakarta.transaction.Transactional;
import kdquiz.ResponseDto;
import kdquiz.domain.Quiz;
import kdquiz.domain.Users;
import kdquiz.game.repository.ParticipantsRepositroy;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameDeleteService {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    ParticipantsRepositroy participantsRepositroy;

    @Transactional
    public ResponseDto<?> GameDelete(Long quizId, Users users){
        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q304","사용자 못 찾음");
        }
        try{
            Optional<Quiz> quizOptional = quizRepository.findByIdAndEmail(quizId, users.getEmail());
            quizOptional.orElseThrow(()->new IllegalArgumentException("종료 할 게임을 찾을 수 없습니다.."));
            participantsRepositroy.deleteAllByQuizId(quizId);
            if (quizOptional.isPresent()) {
                Quiz quiz = quizOptional.get();
                quiz.setPin(0);
                return ResponseDto.setSuccess("G001", "게임 종료 성공", null);

            }else{
                return ResponseDto.setFailed("Q104","삭제 할 퀴즈를 찾을 수 없습니다.");

            }
        }catch (Exception e){
            return ResponseDto.setFailed("G101","게임 종료 실패");

        }
    }
}
