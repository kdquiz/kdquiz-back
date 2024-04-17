package kdquiz.quiz.service;

import kdquiz.domain.*;
import kdquiz.quiz.dto.ChoiceCreateDto;
import kdquiz.quiz.dto.OptionCreateDto;
import kdquiz.quiz.dto.QuestionCreateDto;
import kdquiz.quiz.dto.QuizCreateDto;
import kdquiz.ResponseDto;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.OptionRepository;
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
    OptionRepository optionRepository;

    @Autowired
    ChoiceRepository choiceRepository;

    @Transactional
    public ResponseDto<?> createQuiz(QuizCreateDto quizCreateDto, Users users) {
        try {
            if(users.getEmail()==null){
                return ResponseDto.setSuccess("Q301", "존재하지 않는 회원", null);
            }
            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = new Quiz();
            System.out.println("질문: "+quizCreateDto);
            quiz.setTitle(quizCreateDto.getTitle());
            quiz.setType(quizCreateDto.getType());
            quiz.setCreatedAt(LocalDateTime.now());
            quiz.setEmail(users.getEmail());
            quiz = quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음


            // 질문 및 선택 생성 및 연결
            for (QuestionCreateDto questionDto : quizCreateDto.getQuestions()) {
                // 질문 엔티티 생성 및 저장
                Questions question = new Questions();
                question.setContent(questionDto.getContent());
                question.setCreatedAt(LocalDateTime.now());
                question.setQuiz(quiz); // 퀴즈와 연결
                question = questionRepository.save(question); // 저장 후 ID를 얻기 위해 리턴값으로 받음

                //옵션 저장
                OptionCreateDto optionDto = questionDto.getOptions();
                System.out.println("옵션 출력: "+questionDto.getOptions());

                Options option = new Options();
                option.setUseHint(optionDto.getUseHint());
                option.setHintTime(optionDto.getHintTime());
                option.setHintContent(optionDto.getHintContent());
                option.setUseAiFeedback(optionDto.getUseAiFeedback());
                option.setAiQuestion(optionDto.getAiQuestion());
                option.setCommentary(optionDto.getCommentary());
                option.setScore(optionDto.getScore());
                option.setQuestion(question);
                optionRepository.save(option);


                // 선택 엔티티 생성 및 저장
                for (ChoiceCreateDto choicesDto : questionDto.getChoices()) {
                    Choice choice = new Choice();
                    choice.setContent(choicesDto.getContent());
                    choice.setIsCorrect(choicesDto.getIsCorrect());
                    choice.setQuestion(question); // 질문과 연결
                    choiceRepository.save(choice);
                }
            }

            return ResponseDto.setSuccess("Q001", "퀴즈 생생 성공", null);
        } catch (Exception e) {
            return ResponseDto.setFailed("Q101","퀴즈 생성 실패");

        }
    }
}
