package kdquiz.quiz.service;

import kdquiz.domain.*;
import kdquiz.quiz.dto.*;
import kdquiz.ResponseDto;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.OptionRepository;
import kdquiz.quiz.repository.QuestionRepository;
import kdquiz.quiz.repository.QuizRepository;
import kdquiz.usersecurity.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class QuizUpdateService {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ChoiceRepository choiceRepository;

    @Autowired
    OptionRepository optionRepository;

    @Transactional
    public ResponseDto<Void> QuizUpdate(Long quizId, QuizUpdateDto quizUpdateeDto, Users users) {

        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q303","퀴즈 업데이트 실패");
        }
        try {
            // 기존 퀴즈를 찾음
            Optional<Quiz> quizOptional = quizRepository.findByIdAndEmail(quizId, users.getEmail());
            quizOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

            if (!quizOptional.isPresent()) {
                // 기존 퀴즈가 존재하지 않으면 실패 응답 반환
                return ResponseDto.setFailed("Q103", "퀴즈를 찾을 수 없습니다.");
//                        ResponseDto.<Void>builder()
//                        .code("Q203")
//                        .status(500)
//                        .message("퀴즈를 찾을 수 없습니다.")
//                        .data(null)
//                        .build();
            }

            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = quizOptional.get();
            if(!quizUpdateeDto.getTitle().isEmpty()){
                quiz.setTitle(quizUpdateeDto.getTitle());
            }
            if(!quizUpdateeDto.getType().isEmpty()){
                quiz.setType(quizUpdateeDto.getType());
            }
            quiz.setUpdatedAt(LocalDateTime.now());
            quiz = quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음


            // 질문 및 선택 생성 및 연결
            for (QuestionUpdateDto questionUpdateDto : quizUpdateeDto.getQuestions()) {
                Optional<Questions> questionsOptional = questionRepository.findById(questionUpdateDto.getId());
                // 질문 엔티티 생성 및 저장
                Questions question = questionsOptional.get();
                System.out.println("질문 ID: "+questionUpdateDto.getId());

                question.setContent(questionUpdateDto.getContent());
                question.setUpdatedAt(LocalDateTime.now());
                question.setQuiz(quiz); // 퀴즈와 연결
                question = questionRepository.save(question); // 저장 후 ID를 얻기 위해 리턴값으로 받음
                Long questionId = question.getId();

                //옵션 수정
                OptionUpdateDto optionUpdateDto = questionUpdateDto.getOptions();
                Optional<Options> optionOptional = optionRepository.findById(optionUpdateDto.getId());
                Options option = optionOptional.get();
                option.setUseHint(optionUpdateDto.getUseHint());
                option.setHintTime(optionUpdateDto.getHintTime());
                option.setHintContent(optionUpdateDto.getHintContent());
                option.setUseAiFeedback(optionUpdateDto.getUseAiFeedback());
                option.setAiQuestion(optionUpdateDto.getAiQuestion());
                option.setCommentary(optionUpdateDto.getCommentary());
                option.setScore(optionUpdateDto.getScore());
                option.setQuestion(question);
                optionRepository.save(option);


                for (ChoiceUpdateDto choiceUpdateDto: questionUpdateDto.getChoices()) {
                    Optional<Choice> choiceOptional = choiceRepository.findById(choiceUpdateDto.getId());
                    Choice choice = choiceOptional.get();
                    choice.setContent(choiceUpdateDto.getContent());
                    choice.setIsCorrect(choiceUpdateDto.getIsCorrect());
                    choice.setQuestion(question); // 질문과 연결
                    choiceRepository.save(choice);
                }
            }
            return ResponseDto.setSuccess("Q003", "퀴즈 업데이트 성공", null);
//                    ResponseDto.<Void>builder()
//                    .code("Q003")
//                    .status(200)
//                    .message("퀴즈 업데이트 성공")
//                    .data(null)
//                    .build();
        } catch (Exception e) {
            return ResponseDto.setFailed("Q203","퀴즈 업데이트 실패");
//                    ResponseDto.<Void>builder()
//                    .code("Q103")
//                    .status(500)
//                    .message("퀴즈 업데이트 실패")
//                    .data(null)
//                    .build();
        }
    }
}
