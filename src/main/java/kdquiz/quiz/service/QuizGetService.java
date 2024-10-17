package kdquiz.quiz.service;

import kdquiz.ResponseDto;
import kdquiz.domain.*;
import kdquiz.quiz.dto.Choice.ChoiceGetDto;
import kdquiz.quiz.dto.Img.ImgGetDto;
import kdquiz.quiz.dto.Option.OptionGetDto;
import kdquiz.quiz.dto.Question.QuestionGetDto;
import kdquiz.quiz.dto.Quiz.QuizGetAllDto;
import kdquiz.quiz.dto.Quiz.QuizGetDto;
import kdquiz.quiz.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizGetService {

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;

    private final OptionRepository optionRepository;

    private final ChoiceRepository choiceRepository;

    private final QuizImgRepository quizImgRepository;

    private final QuizUpdateService quizUpdateService;


    public QuizGetService(QuizRepository quizRepository, QuestionRepository questionRepository, OptionRepository optionRepository, ChoiceRepository choiceRepository, QuizImgRepository quizImgRepository, QuizUpdateService quizUpdateService) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.optionRepository = optionRepository;
        this.choiceRepository = choiceRepository;
        this.quizImgRepository = quizImgRepository;
        this.quizUpdateService = quizUpdateService;
    }

    @Transactional
    public ResponseDto<List<QuizGetAllDto>> quizGetAll(Users users, String SortBy, String searchTitle){
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
    public ResponseDto<QuizGetDto> quizGet(Long quizId, Users users){
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

            // Quiz와 관련된 Questions 목록을 가져옵니다.
            List<Question> questionList = questionRepository.findByQuiz_Id(quiz.getId());
            List<QuestionGetDto> questionDtos = new ArrayList<>();

            for (Question question : questionList) {
                QuestionGetDto questionGetDto = new QuestionGetDto();
                questionGetDto.setId(question.getId());
                questionGetDto.setContent(question.getContent());
                questionGetDto.setOrd(question.getOrd());
                questionGetDto.setType(question.getType());
                questionGetDto.setShortAnswer(question.getShortAnswer());

                // Options 정보를 QuestionGetDto에 추가
                Options options = question.getOptions();
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
                List<Choice> choicesList = choiceRepository.findByQuestion_Id(question.getId());
                List<ChoiceGetDto> choiceDtos = new ArrayList<>();

                for (Choice choice : choicesList) {
                    ChoiceGetDto choiceGetDto = new ChoiceGetDto();
                    choiceGetDto.setId(choice.getId());
                    choiceGetDto.setContent(choice.getContent());
                    choiceGetDto.setIsCorrect(choice.getIsCorrect());
                    choiceGetDto.setShortAnswer(choice.getShortAnswer());
                    choiceDtos.add(choiceGetDto);
                }
                questionGetDto.setChoices(choiceDtos);

                List<QuizImg> quizImgList = quizImgRepository.findByQuestionId(question.getId());
                List<ImgGetDto> ImgList = new ArrayList<>();
                if(quizImgList != null && !quizImgList.isEmpty()){
                    for(QuizImg quizImg : quizImgList){
                        ImgGetDto imgGetDto = new ImgGetDto();
                        imgGetDto.setFileName(quizImg.getFileName());
                        ImgList.add(imgGetDto);
                    }
                }
                questionGetDto.setUploadFileNames(ImgList);
                questionDtos.add(questionGetDto);
            }

            quizGetDto.setQuestions(questionDtos);

            return ResponseDto.setSuccess("Q002", "사용자가 생성한 퀴즈 목록 조회 성공", quizGetDto);
        } catch (Exception e) {
            return ResponseDto.setFailed("Q202", "사용자가 생성한 퀴즈 목록 조회 실패");
        }

    }

}
