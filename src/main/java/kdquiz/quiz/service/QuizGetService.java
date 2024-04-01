package kdquiz.quiz.service;

import kdquiz.quiz.domain.Quiz;
import kdquiz.quiz.dto.GetQuizDto;
import kdquiz.quiz.exception.ResponseDto;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuizGetService {
    @Autowired
    QuizRepository quizRepository;

    @Transactional
    public ResponseDto<List<GetQuizDto>> GetQuiz(Long userId){
        try{
            List<Quiz> quizzes = quizRepository.findByUsers_Id(userId);

            //user id를 못 찾을 경우
            if(quizzes.isEmpty()){
                return ResponseDto.<List<GetQuizDto>>builder()
                        .code("Q202")
                        .status(500)
                        .message("사용자가 없음")
                        .data(null)
                        .build();
            }

            List<GetQuizDto> getList = new ArrayList<>();
            for(Quiz quiz : quizzes){
                GetQuizDto getQuiz = new GetQuizDto();
                getQuiz.setId(quiz.getUsers().getId());
                getQuiz.setTitle(quiz.getTitle());
                getQuiz.setType(quiz.getType());
                getList.add(getQuiz);
            }
            return ResponseDto.<List<GetQuizDto>>builder()
                    .code("Q002")
                    .status(200)
                    .message("사용자가 생성한 퀴즈 목록 조회 성공")
                    .data(getList)
                    .build();
        } catch (Exception e) {
            return ResponseDto.<List<GetQuizDto>>builder()
                    .code("Q202")
                    .status(500)
                    .message("사용자가 생성한 퀴즈 목록 조회 실패")
                    .data(null)
                    .build();
        }

    }

}
