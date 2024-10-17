package kdquiz.quiz.service;

import kdquiz.domain.Quiz;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class QuestionSizeService {
    @Autowired
    private QuizRepository quizRepository;

    public int countQuestion(Long quizId){
        Optional<Quiz> quiz = quizRepository.findById(quizId);
        if(quiz.isPresent()){
            return quiz.get().getQuestions().size();
        }
        return 0;
    }
}
