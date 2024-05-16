package kdquiz.quiz.service;

import kdquiz.ResponseDto;
import kdquiz.domain.*;
import kdquiz.quiz.dto.*;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.OptionRepository;
import kdquiz.quiz.repository.QuestionRepository;
import kdquiz.quiz.repository.QuizRepository;
import kdquiz.users.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuestionCRUDService {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ChoiceRepository choiceRepository;

    @Autowired
    OptionRepository optionRepository;

    @Autowired
    UsersRepository usersRepository;

    //이미지 저장 경로
    @Value("${image.upload.path}")
    private String imageUploadPath;

    private String getImageUrl(MultipartFile file, long id) throws IOException {

        String UserId = Long.toString(id);
        // 파일의 원래 이름을 가져옴
        String originalFilename = file.getOriginalFilename();
        // 파일의 확장자를 추출함
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
        // 저장될 파일 이름을 생성함 (예: "image1234.jpg")
        String savedFileName = UserId +"_"+ System.currentTimeMillis() + fileExtension;
        // 이미지가 저장될 경로를 지정함
        String imagePath = imageUploadPath + savedFileName;

        // 이미지를 서버에 저장함
        Path path = Paths.get(imagePath);
        Files.write(path, file.getBytes());

        // 저장된 이미지의 URL을 생성하여 반환함
        String imageUrl = "/"+ UserId+ "/" + savedFileName; // 예시: "/images/image1234.jpg"

        return imageUrl;
    }

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

            }
            return ResponseDto.setSuccess("Q003", "퀴즈 업데이트 성공", null);
        } catch (Exception e) {
            return ResponseDto.setFailed("Q203","퀴즈 업데이트 실패");
        }
    }

    //Question 수정
    @Transactional
    public ResponseDto<Void> QuestionUpdate(Long questionId, QuizUpDto quizUpDto, MultipartFile[] files, Users users) {

        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try {
            // 기존 퀴즈를 찾음
            Optional<Questions> questionsOptional = questionRepository.findById(questionId);
            questionsOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

            Questions questions = questionsOptional.get();
            Long QuizId = questions.getQuiz().getId();
            Optional<Quiz> quizOptional = quizRepository.findById(QuizId);
            Quiz quiz = quizOptional.get();

            Optional<Users> user = usersRepository.findByEmail(users.getEmail());
            Users userId  = user.get();

            int i=0;
            // 질문 및 선택 생성 및 연결
            for (QuestionUpdateDto questionUpdateDto : quizUpDto.getQuestions()) {
                // 질문 엔티티 생성 및 저장
                Questions question = questionsOptional.get();
                if(questionUpdateDto.getImgTF()==true){
                    if (files != null && i < files.length) {
                        MultipartFile file = files[i];
                        String imageUrl = getImageUrl(file, userId.getId());
                        i++;
                        question.setImg(imageUrl);

                    }
                }
                question.setContent(questionUpdateDto.getContent());
                question.setUpdatedAt(LocalDateTime.now());
                question.setQuiz(quiz); // 퀴즈와 연결
                questionRepository.save(question);
                question.setContent(questionUpdateDto.getContent());
                question.setUpdatedAt(LocalDateTime.now());
                question.setQuiz(quiz); // 퀴즈와 연결
                question = questionRepository.save(question); // 저장 후 ID를 얻기 위해 리턴값으로 받음


                //옵션 수정
                OptionUpdateDto optionUpdateDto = questionUpdateDto.getOptions();
                Optional<Options> optionOptional = optionRepository.findById(optionUpdateDto.getId());
                Options option = optionOptional.get();
                option.setTime(optionUpdateDto.getTime());
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
                    choice.setShortAnswer(choiceUpdateDto.getShortAnswer());
                    choice.setQuestion(question); // 질문과 연결
                    choiceRepository.save(choice);
                }
            }
            return ResponseDto.setSuccess("Q003", "질문 생성 성공", null);
        } catch (Exception e) {
            return ResponseDto.setFailed("Q203","질문 생성 실패");

        }
    }

    //Question 삭제
    @Transactional
    public ResponseDto<Void> QuestionDelete(Long questionId, Users users) {

        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q203","사용자 정보 가져오기 실패");
        }

        try {
            // 기존 퀴즈를 찾음
            Optional<Questions> questions = questionRepository.findById(questionId);
            Questions question = questions.get();

            questionRepository.delete(questions.get());

            Optional<Quiz> quizOptional = quizRepository.findByEmailAndId(users.getEmail(), question.getQuiz().getId());
            quizOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = quizOptional.get();
            quiz.setUpdatedAt(LocalDateTime.now());
            quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음

            return ResponseDto.setSuccess("Q003", "질문 삭제 성공", null);

        } catch (Exception e) {
            return ResponseDto.setFailed("Q103","질문 삭제 실패");
        }
    }

    @Transactional
    public ResponseDto<QuestionGetDto> QuestionGet(Long questionId, Users users){
        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try{
            Optional<Questions> questionsOptional = questionRepository.findById(questionId);
            questionsOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

            Questions questions = questionsOptional.get();
            // Quiz와 관련된 Questions 목록을 가져옵니다.

            QuestionGetDto questionDtos = new QuestionGetDto();

            QuestionGetDto questionGetDto = new QuestionGetDto();
            questionGetDto.setId(questions.getId());
            questionGetDto.setContent(questions.getContent());
            questionGetDto.setImgUrl(questions.getImg());

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
                choiceGetDto.setShortAnswer(choice.getShortAnswer());
                choiceDtos.add(choiceGetDto);
            }

            questionGetDto.setChoices(choiceDtos);

            return ResponseDto.setSuccess("Q002", "사용자가 생성한 질문 조회 성공", questionGetDto);
        } catch (Exception e) {
            return ResponseDto.setFailed("Q202", "사용자가 생성한 질문 조회 실패");
        }
    }
}
