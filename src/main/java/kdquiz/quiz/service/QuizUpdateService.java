package kdquiz.quiz.service;

import kdquiz.quiz.domain.Choice;
import kdquiz.quiz.domain.Questions;
import kdquiz.quiz.domain.Quiz;
import kdquiz.quiz.dto.ChoiceUpdateDto;
import kdquiz.quiz.dto.QuestionUpdateDto;
import kdquiz.quiz.dto.QuizUpdateDto;
import kdquiz.quiz.exception.ResponseDto;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.QuestionRepository;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Optional;

@Service
public class QuizUpdateService {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ChoiceRepository choiceRepository;

    @Transactional
    public ResponseDto<Void> QuizUpdate(Long quizId, QuizUpdateDto quizUpdateeDto) {


        try {
            // 기존 퀴즈를 찾음
            Optional<Quiz> quizOptional = quizRepository.findById(quizId);
            if (!quizOptional.isPresent()) {
                // 기존 퀴즈가 존재하지 않으면 실패 응답 반환
                return ResponseDto.<Void>builder()
                        .code("Q203")
                        .status(500)
                        .message("퀴즈를 찾을 수 없습니다.")
                        .data(null)
                        .build();
            }

            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = quizOptional.get();
            if(!quizUpdateeDto.getTitle().isEmpty()){
                quiz.setTitle(quizUpdateeDto.getTitle());
            }
            if(!quizUpdateeDto.getType().isEmpty()){
                quiz.setType(quizUpdateeDto.getType());
            }
            quiz = quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음


            // 질문 및 선택 생성 및 연결
            for (QuestionUpdateDto questionUpdateDto : quizUpdateeDto.getQuestions()) {
                Optional<Questions> questionsOptional = questionRepository.findById(questionUpdateDto.getId());
                // 질문 엔티티 생성 및 저장
                Questions question = questionsOptional.get();
                System.out.println("질문 ID: "+questionUpdateDto.getId());
                if(!question.getContent().isEmpty()){
                    question.setContent(questionUpdateDto.getContent());
                }
                question.setScore(questionUpdateDto.getScore());
                question.setQuiz(quiz); // 퀴즈와 연결
                question = questionRepository.save(question); // 저장 후 ID를 얻기 위해 리턴값으로 받음

                Long questionId = question.getId();
                System.out.println("질문아이디: "+questionId);

                for (ChoiceUpdateDto choiceUpdateDto: questionUpdateDto.getChoices()) {
                    Optional<Choice> choiceOptional = choiceRepository.findById(choiceUpdateDto.getId());
                    System.out.println("선택 ID: "+choiceUpdateDto.getId());
                    Choice choice = choiceOptional.get();
                    choice.setContent(choiceUpdateDto.getContent());
                    choice.setIsCorrect(choiceUpdateDto.getIsCorrect());
                    choice.setQuestion(question); // 질문과 연결
                    choiceRepository.save(choice);
                }
            }
            return ResponseDto.<Void>builder()
                    .code("Q00")
                    .status(200)
                    .message("퀴즈 업데이트 성공")
                    .data(null)
                    .build();
        } catch (Exception e) {
            return ResponseDto.<Void>builder()
                    .code("Q101")
                    .status(500)
                    .message("퀴즈 업데이트 실패")
                    .data(null)
                    .build();
        }
    }
}
