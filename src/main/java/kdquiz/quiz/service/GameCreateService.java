package kdquiz.quiz.service;

import jakarta.transaction.Transactional;
import kdquiz.ResponseDto;
import kdquiz.domain.Quiz;
import kdquiz.domain.Users;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Random;

@Service
public class GameCreateService {

    @Autowired
    QuizRepository quizRepository;

    private int authNumber=0;

    //임의의 6자리 양수를 반환합니다.
    public void GamePiNmNumber(Long Id){
        Random r = new Random();
        String randomNumber = "";
        for(int i=0; i<6; i++){
            randomNumber += Integer.toString(r.nextInt(10))+Long.toString(Id);
        }
        authNumber = Integer.parseInt(randomNumber);
    }

    @Transactional
    public ResponseDto<?> GameCreate(Long quizId, Users users){
        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q105", "존재하지 않는 회원");
        }
        System.out.println("게임 생성자 메일: "+users.getEmail());
        System.out.println("생성자 고유 번호: "+users.getId());
        try{
            Optional<Quiz> quizOptional = quizRepository.findByIdAndEmail(quizId, users.getEmail());
            quizOptional.orElseThrow(()-> new IllegalArgumentException("해당 퀴즈가 존재하지 않습니다."));
            GamePiNmNumber(users.getId());
//            if(authNumber==0){
//                return ResponseDto.setFailed("Q305", "게임 생성 실패");
//            }
            System.out.println("게임 번호: "+authNumber);
            Quiz quiz = quizOptional.get();
            quiz.setPin(authNumber);
            return ResponseDto.setSuccess("Q005", "게임 생성 성공", authNumber);
        } catch (Exception e) {
            return ResponseDto.setFailed("Q202", "게임 생성 실패");
        }


    }
}
