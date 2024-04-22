package kdquiz.quiz.service;

import kdquiz.domain.*;
import kdquiz.quiz.dto.*;
import kdquiz.ResponseDto;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.OptionRepository;
import kdquiz.quiz.repository.QuestionRepository;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizGetService {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    ChoiceRepository choiceRepository;
    @Autowired
    private QuizUpdateService quizUpdateService;

    @Transactional
    public ResponseDto<List<QuizGetAllDto>> QuizGetAll(Users users, String SortBy, String searchTitle){
        try{
            List<Quiz> quizzes;

            if(searchTitle != null && !searchTitle.isEmpty()){
                quizzes = quizRepository.findByEmailAndTitleContaining(users.getEmail(), searchTitle);
            }else {
                quizzes = quizRepository.findByEmail(users.getEmail());
            }


            //user id를 못 찾을 경우
            if(quizzes.isEmpty()){
                return ResponseDto.setFailed("Q102","퀴즈가 없음");
            }
            List<QuizGetAllDto> getList = new ArrayList<>();
            for(Quiz quiz : quizzes){
                QuizGetAllDto getQuiz = new QuizGetAllDto();
                getQuiz.setId(quiz.getId());
                getQuiz.setTitle(quiz.getTitle());
                getQuiz.setType(quiz.getType());
                getQuiz.setCreate_at(quiz.getCreatedAt());
                getQuiz.setUpdate_at(quiz.getUpdatedAt());
                getList.add(getQuiz);
            }
            if(SortBy.equals("Time_asc")){
                getList.sort((q1, q2)->q1.getCreate_at().compareTo(q2.getCreate_at()));
            }else if(SortBy.equals("Time_desc")){
                getList.sort((q1, q2)->q2.getCreate_at().compareTo(q1.getCreate_at()));
            }
            if(SortBy.equals("asc")){
                getList.sort((q1, q2)->q1.getTitle().compareTo(q2.getTitle()));
            }else if(SortBy.equals("desc")){
                getList.sort((q1, q2)->q2.getTitle().compareTo(q1.getTitle()));
            }
            return ResponseDto.setSuccess("Q002", "사용자가 생성한 퀴즈 목록 조회 성공", getList);

        } catch (Exception e) {
            return ResponseDto.setFailed("Q202","사용자가 없음");

        }

    }


    @Transactional
    public ResponseDto<QuizGetDto> QuizGet(Long quizId, Users users){
        try{
            Optional<Quiz> quizOptional = quizRepository.findByEmailAndId(users.getEmail(), quizId);

            // 사용자 ID를 찾지 못한 경우 예외 처리
            if (quizOptional.isEmpty()) {
                return ResponseDto.setFailed("Q102", "사용자가 없음");
            }

            Quiz quiz = quizOptional.get();
            System.out.println("퀴즈 정보: " + quiz.getTitle());

            // QuizGetDto 객체 생성 및 퀴즈 제목과 유형 설정
            QuizGetDto quizGetDto = new QuizGetDto();
            quizGetDto.setId(quiz.getId());
            quizGetDto.setTitle(quiz.getTitle());
            quizGetDto.setType(quiz.getType());

            // Quiz와 관련된 Questions 목록을 가져옵니다.
            List<Questions> questionsList = questionRepository.findByQuiz_Id(quiz.getId());
            List<QuestionGetDto> questionDtos = new ArrayList<>();

            for (Questions questions : questionsList) {
                QuestionGetDto questionGetDto = new QuestionGetDto();
                questionGetDto.setId(questions.getId());
                questionGetDto.setContent(questions.getContent());

                // Options 정보를 QuestionGetDto에 추가
                Options options = questions.getOption();
                OptionGetDto optionGetDto = new OptionGetDto();
                optionGetDto.setId(options.getId());
                optionGetDto.setTime(options.getTime());
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
                    choiceGetDto.setId(choice.getId());
                    choiceGetDto.setContent(choice.getContent());
                    choiceGetDto.setIsCorrect(choice.getIsCorrect());
                    choiceDtos.add(choiceGetDto);
                }

                questionGetDto.setChoices(choiceDtos);
                questionDtos.add(questionGetDto);
            }

            quizGetDto.setQuestions(questionDtos);

            return ResponseDto.setSuccess("Q002", "사용자가 생성한 퀴즈 목록 조회 성공", quizGetDto);
        } catch (Exception e) {
            return ResponseDto.setFailed("Q202", "사용자가 생성한 퀴즈 목록 조회 실패");
        }

    }

}
