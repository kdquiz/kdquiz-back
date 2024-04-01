package kdquiz.quiz.service;

import kdquiz.quiz.domain.Choice;
import kdquiz.quiz.domain.Questions;
import kdquiz.quiz.domain.Quiz;
import kdquiz.quiz.dto.ChoicesCreateDto;
import kdquiz.quiz.dto.QuestionCreateDto;
import kdquiz.quiz.dto.QuizCreateDto;
import kdquiz.quiz.exception.ResponseDto;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.QuestionRepository;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;


@Service
public class QuizCreateService {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ChoiceRepository choiceRepository;

    @Transactional
    public ResponseDto<?> createQuiz(QuizCreateDto quizCreateDto) {
        try {
            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = new Quiz();

            quiz.setTitle(quizCreateDto.getTitle());
            quiz.setType(quizCreateDto.getType());
            quiz.setCreatedAt(LocalDateTime.now());
            quiz = quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음


            // 질문 및 선택 생성 및 연결
            for (QuestionCreateDto questionDto : quizCreateDto.getQuestions()) {
                // 질문 엔티티 생성 및 저장
                Questions question = new Questions();
                question.setContent(questionDto.getContent());
                question.setScore(questionDto.getScore());
                question.setQuiz(quiz); // 퀴즈와 연결
                question = questionRepository.save(question); // 저장 후 ID를 얻기 위해 리턴값으로 받음

                // 선택 엔티티 생성 및 저장
                for (ChoicesCreateDto choicesDto : questionDto.getChoices()) {
                    Choice choice = new Choice();
                    choice.setContent(choicesDto.getContent());
                    choice.setIsCorrect(choicesDto.getIsCorrect());
                    choice.setQuestion(question); // 질문과 연결
                    choiceRepository.save(choice);
                }
            }

            return ResponseDto.<Void>builder()
                    .code("Q001")
                    .status(200)
                    .message("퀴즈 생성 성공")
                    .data(null)
                    .build();
        } catch (Exception e) {
            return ResponseDto.<Void>builder()
                    .code("Q101")
                    .status(500)
                    .message("퀴즈 생성 실패")
                    .data(null)
                    .build();
        }
    }
}
