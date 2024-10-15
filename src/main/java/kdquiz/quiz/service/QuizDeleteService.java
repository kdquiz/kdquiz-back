package kdquiz.quiz.service;

import jakarta.transaction.Transactional;
import kdquiz.domain.Question;
import kdquiz.domain.Quiz;
import kdquiz.ResponseDto;
import kdquiz.domain.QuizImg;
import kdquiz.domain.Users;
import kdquiz.quiz.repository.QuestionRepository;
import kdquiz.quiz.repository.QuizImgRepository;
import kdquiz.quiz.repository.QuizRepository;
import kdquiz.util.CustomFileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuizDeleteService {

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;

    private final QuizImgRepository quizImgRepository;

    private final CustomFileUtil customFileUtil;

    public QuizDeleteService(QuizRepository quizRepository, QuestionRepository questionRepository, QuizImgRepository quizImgRepository, CustomFileUtil customFileUtil) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.quizImgRepository = quizImgRepository;
        this.customFileUtil = customFileUtil;
    }


    @Transactional
    public ResponseDto<?> QuizDelete(Long quizId, Users users){
        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q304","사용자 못 찾음");
        }
        try{
            Optional<Quiz> quizOptional = quizRepository.findByIdAndEmail(quizId, users.getEmail());
            quizOptional.orElseThrow(()->new IllegalArgumentException("삭제 할 퀴즈를 찾을 수 없습니다."));

            //이미지 삭제
            List<Question> questionList = questionRepository.findByQuiz_Id(quizId);
            List<String> imgList = new ArrayList<>();
            if(questionList != null && !questionList.isEmpty()){
                for(Question question : questionList){
                    List<QuizImg> quizImgs = quizImgRepository.findByQuestionId(question.getId());
                    if(quizImgs!=null && !quizImgs.isEmpty()){
                        imgList = quizImgs.stream()
                                .map(QuizImg::getFileName)
                                .collect(Collectors.toList());
                        customFileUtil.deleteFiles(imgList);

                    }
                }
            }
            if(imgList != null && !imgList.isEmpty()){
                for(String name : imgList){
                    Optional<QuizImg> quizImg = quizImgRepository.findById(name);
                    quizImgRepository.delete(quizImg.get());
                }
            }
            quizRepository.delete(quizOptional.get());

            return ResponseDto.setSuccess("Q004", "퀴즈 삭제 성공", null);
        }catch (Exception e){
            return ResponseDto.setFailed("Q204","퀴즈 삭제 실패");
        }
    }
}
