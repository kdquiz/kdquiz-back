package kdquiz.game.service;

import kdquiz.ResponseDto;
import kdquiz.domain.*;
import kdquiz.game.dto.GameCreateDto;
import kdquiz.quiz.dto.*;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.OptionRepository;
import kdquiz.quiz.repository.QuestionRepository;
import kdquiz.quiz.repository.QuizRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class GameCreateService {

    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    ChoiceRepository choiceRepository;

    public String generateRandomPin(Long id) {
        String randomPin = RandomStringUtils.randomNumeric(6); // 6자리 숫자로 이루어진 랜덤 문자열 생성

        return id + randomPin;
    }

    @Transactional
    public ResponseDto<GameCreateDto> GameGet(Long quizId, Users users){
        try{
            Optional<Quiz> quizOptional = quizRepository.findByEmailAndId(users.getEmail(), quizId);

            // 사용자 ID를 찾지 못한 경우 예외 처리
            if (quizOptional.isEmpty()) {
                return ResponseDto.setFailed("G102", "사용자가 없음");
            }

            Quiz quiz = quizOptional.get();
            System.out.println("퀴즈 정보: " + quiz.getTitle());

            // QuizGetDto 객체 생성 및 퀴즈 제목과 유형 설정
            String gamePin = generateRandomPin(users.getId());
            GameCreateDto gameCreateDto = new GameCreateDto();
            gameCreateDto.setTitle(quiz.getTitle());
            gameCreateDto.setType(quiz.getType());
            gameCreateDto.setPin(gamePin);
            quiz.setPin(Integer.parseInt(gamePin));

            // Quiz와 관련된 Questions 목록을 가져옵니다.
            List<Questions> questionsList = questionRepository.findByQuiz_Id(quiz.getId());
            List<QuestionGetDto> questionDtos = new ArrayList<>();

            for (Questions questions : questionsList) {
                QuestionGetDto questionGetDto = new QuestionGetDto();
                questionGetDto.setContent(questions.getContent());

                // Options 정보를 QuestionGetDto에 추가
                Options options = questions.getOption();
                OptionGetDto optionGetDto = new OptionGetDto();
                optionGetDto.setUseHint(options.getUseHint());
                optionGetDto.setHintTime(options.getHintTime());
                optionGetDto.setHintContent(options.getHintContent());
                optionGetDto.setUseAiFeedback(options.getUseAiFeedback());
                optionGetDto.setAiQuestion(options.getAiQuestion());
                optionGetDto.setCommentary(options.getCommentary());
                optionGetDto.setScore(options.getScore());

                questionGetDto.setOptions(optionGetDto);

                // Choices 목록을 가져와서 QuestionGetDto에 추가
                List<Choice> choicesList = choiceRepository.findByQuestion_Id(questions.getId());
                List<ChoiceGetDto> choiceDtos = new ArrayList<>();

                for (Choice choice : choicesList) {
                    ChoiceGetDto choiceGetDto = new ChoiceGetDto();
                    choiceGetDto.setContent(choice.getContent());
                    choiceGetDto.setIsCorrect(choice.getIsCorrect());
                    choiceDtos.add(choiceGetDto);
                }

                questionGetDto.setChoices(choiceDtos);
                questionDtos.add(questionGetDto);
            }

            gameCreateDto.setQuestions(questionDtos);

            return ResponseDto.setSuccess("G002", "사용자가 생성 성공", gameCreateDto);
        } catch (Exception e) {
            return ResponseDto.setFailed("G202", "게임 생성 실패");
        }

    }
}
