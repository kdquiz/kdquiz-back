package kdquiz.quiz.service;

import kdquiz.ResponseDto;
import kdquiz.domain.*;
import kdquiz.quiz.dto.Quiz.QuizCreateDto;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.OptionRepository;
import kdquiz.quiz.repository.QuestionRepository;
import kdquiz.quiz.repository.QuizRepository;
import kdquiz.users.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;


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

    @Autowired
    UsersRepository usersRepository;


//    @Transactional
//    public ResponseDto<?> createQuiz(QuizCreateDto quizCreateDto, MultipartFile[] files, Users users) {
//        try {
//            if(users.getEmail()==null){
//                return ResponseDto.setSuccess("Q301", "존재하지 않는 회원", null);
//            }
//            Optional<Users> user = usersRepository.findByEmail(users.getEmail());
//            Users userId  = user.get();
//            // 퀴즈 엔티티 생성 및 저장
//            Quiz quiz = new Quiz();
//            System.out.println("질문: "+quizCreateDto);
//            quiz.setTitle(quizCreateDto.getTitle());
//            quiz.setType(quizCreateDto.getType());
//            quiz.setCreatedAt(LocalDateTime.now());
//            quiz.setEmail(users.getEmail());
//            quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음
//            if(quizCreateDto.getQuestions().isEmpty()){
//                Questions questions = new Questions();
//                questions.setContent("");
//                questions.setCreatedAt(LocalDateTime.now());
//            }
//            int i=0;
//            // 질문 및 선택 생성 및 연결
//            for (QuestionCreateDto questionDto : quizCreateDto.getQuestions()) {
//
//                // 질문 엔티티 생성 및 저장
//                Questions question = new Questions();
//                if(questionDto.getImgTF()==true) {
//                    if (files != null && i < files.length) {
//                        MultipartFile file = files[i];
//                        String imageUrl = getImageUrl(file, userId.getId());
//                        i++;
//                        question.setImg(imageUrl);
//
//                    }
//                }
//                question.setContent(questionDto.getContent());
//                question.setCreatedAt(LocalDateTime.now());
//                question.setQuiz(quiz); // 퀴즈와 연결
//                question = questionRepository.save(question); // 저장 후 ID를 얻기 위해 리턴값으로 받음
//
//                //옵션 저장
//                OptionCreateDto optionDto = questionDto.getOptions();
//                System.out.println("옵션 출력: "+questionDto.getOptions());
//
//                Options option = new Options();
//                option.setTime(optionDto.getTime());
//                option.setUseHint(optionDto.getUseHint());
//                option.setHintTime(optionDto.getHintTime());
//                option.setHintContent(optionDto.getHintContent());
//                option.setUseAiFeedback(optionDto.getUseAiFeedback());
//                option.setAiQuestion(optionDto.getAiQuestion());
//                option.setCommentary(optionDto.getCommentary());
//                option.setScore(optionDto.getScore());
//                option.setQuestion(question);
//                optionRepository.save(option);
//
//                // 선택 엔티티 생성 및 저장
//                for (ChoiceCreateDto choicesDto : questionDto.getChoices()) {
//                    Choice choice = new Choice();
//                    choice.setContent(choicesDto.getContent());
//                    choice.setIsCorrect(choicesDto.getIsCorrect());
//                    choice.setShortAnswer(choicesDto.getShortAnswer());
//                    choice.setQuestion(question); // 질문과 연결
//                    choiceRepository.save(choice);
//                }
//            }
//
//            return ResponseDto.setSuccess("Q001", "퀴즈 생생 성공", null);
//        } catch (Exception e) {
//            return ResponseDto.setFailed("Q101","퀴즈 생성 실패");
//
//        }
//    }

    @Transactional
    public ResponseDto<?> createQuiz(QuizCreateDto quizCreateDto, Users users) {
        try {
            if(users.getEmail()==null){
                return ResponseDto.setSuccess("Q301", "존재하지 않는 회원", null);
            }
            Optional<Users> user = usersRepository.findByEmail(users.getEmail());
            Users userId  = user.get();
            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = new Quiz();
            System.out.println("질문: "+quizCreateDto);
            quiz.setTitle(quizCreateDto.getTitle());
            quiz.setCreatedAt(LocalDateTime.now());
            quiz.setEmail(users.getEmail());
            quizRepository.save(quiz);


            // 질문 엔티티 생성 및 저장
            Question question = new Question();
            question.setContent("문제");
            question.setCreatedAt(LocalDateTime.now());
            question.setQuiz(quiz); // 퀴즈와 연결
            questionRepository.save(question);

            Options options = new Options();
            options.setUseHint(true);
            options.setHintTime(30);
            options.setHintContent("힌트");
            options.setUseAiFeedback(true);
            options.setAiQuestion("ai 피드백");
            options.setCommentary("코멘트 피드백");
            options.setScore(20);
            options.setQuestion(question);
            optionRepository.save(options);

            for(int i=1; i<=4; i++){
                String num = Integer.toString(i);
                Choice choice = new Choice();
                choice.setContent(num);
                choice.setIsCorrect(false);
                choice.setShortAnswer("단답형");
                choice.setQuestion(question);
                choiceRepository.save(choice);
            }

            return ResponseDto.setSuccess("Q001", "퀴즈 생생 성공", null);
        } catch (Exception e) {
            return ResponseDto.setFailed("Q101","퀴즈 생성 실패");

        }
    }
}
