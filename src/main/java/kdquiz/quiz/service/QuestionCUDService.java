package kdquiz.quiz.service;

import kdquiz.ResponseDto;
import kdquiz.domain.Questions;
import kdquiz.domain.Quiz;
import kdquiz.domain.Users;
import kdquiz.quiz.dto.QuestionCrDto;
import kdquiz.quiz.dto.QuestionUpDto;
import kdquiz.quiz.dto.QuizCrDto;
import kdquiz.quiz.dto.QuizUpDto;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.OptionRepository;
import kdquiz.quiz.repository.QuestionRepository;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class QuestionCUDService {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ChoiceRepository choiceRepository;

    @Autowired
    OptionRepository optionRepository;

    //Question 추가
    @Transactional
    public ResponseDto<Void> QuestionCreate(Long quizId, QuizCrDto quizCrDto, Users users) {

        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }

        try {
            // 기존 퀴즈를 찾음
            Optional<Quiz> quizOptional = quizRepository.findByEmailAndId(users.getEmail(), quizId);
            quizOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = quizOptional.get();
            quiz.setUpdatedAt(LocalDateTime.now());
            quiz = quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음


            // 질문 및 선택 생성 및 연결
            for (QuestionCrDto questionCrDto : quizCrDto.getQuestions()) {

                // 질문 엔티티 생성 및 저장
                Questions question = new Questions();
                question.setContent(questionCrDto.getContent());
                question.setCreatedAt(LocalDateTime.now());
                question.setQuiz(quiz); // 퀴즈와 연결
                questionRepository.save(question);

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

    //Question 수정
    @Transactional
    public ResponseDto<Void> QuestionUpdate(Long quizId, QuizUpDto quizUpDto, Users users) {

        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }

        try {
            // 기존 퀴즈를 찾음
            Optional<Quiz> quizOptional = quizRepository.findByEmailAndId(users.getEmail(), quizId);
            quizOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = quizOptional.get();
            quiz.setUpdatedAt(LocalDateTime.now());
            quiz = quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음


            // 질문 및 선택 생성 및 연결
            for (QuestionUpDto questionUpDto : quizUpDto.getQuestions()) {
                Optional<Questions> questionsOptional = questionRepository.findById(questionUpDto.getId());
                // 질문 엔티티 생성 및 저장
                Questions question = questionsOptional.get();
                System.out.println("질문 ID: "+questionUpDto.getId());
                question.setContent(questionUpDto.getContent());
                question.setUpdatedAt(LocalDateTime.now());
                question.setQuiz(quiz); // 퀴즈와 연결
                questionRepository.save(question);

            }
            return ResponseDto.setSuccess("Q003", "질문 생성 성공", null);
//                    ResponseDto.<Void>builder()
//                    .code("Q003")
//                    .status(200)
//                    .message("퀴즈 업데이트 성공")
//                    .data(null)
//                    .build();
        } catch (Exception e) {
            return ResponseDto.setFailed("Q203","질문 생성 실패");
//                    ResponseDto.<Void>builder()
//                    .code("Q103")
//                    .status(500)
//                    .message("퀴즈 업데이트 실패")
//                    .data(null)
//                    .build();
        }
    }

    //Question 삭제
    @Transactional
    public ResponseDto<Void> QuestionDelete(Long quizId, Long questionId, Users users) {

        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q203","사용자 정보 가져오기 실패");
        }

        try {
            // 기존 퀴즈를 찾음
            Optional<Quiz> quizOptional = quizRepository.findByEmailAndId(users.getEmail(), quizId);
            quizOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = quizOptional.get();
            quiz.setUpdatedAt(LocalDateTime.now());
            quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음
            Optional<Questions> questions = questionRepository.findById(questionId);
            questionRepository.delete(questions.get());
            return ResponseDto.setSuccess("Q003", "질문 삭제 성공", null);
//                    ResponseDto.<Void>builder()
//                    .code("Q003")
//                    .status(200)
//                    .message("퀴즈 업데이트 성공")
//                    .data(null)
//                    .build();
        } catch (Exception e) {
            return ResponseDto.setFailed("Q103","질문 삭제 실패");
//                    ResponseDto.<Void>builder()
//                    .code("Q103")
//                    .status(500)
//                    .message("퀴즈 업데이트 실패")
//                    .data(null)
//                    .build();
        }
    }

}
