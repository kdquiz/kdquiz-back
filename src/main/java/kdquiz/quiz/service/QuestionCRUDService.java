package kdquiz.quiz.service;

import kdquiz.ResponseDto;
import kdquiz.domain.*;
import kdquiz.quiz.dto.Choice.ChoiceGetDto;
import kdquiz.quiz.dto.Choice.ChoiceUpdateDto;
import kdquiz.quiz.dto.Img.ImgGetDto;
import kdquiz.quiz.dto.Option.OptionGetDto;
import kdquiz.quiz.dto.Option.OptionUpdateDto;
import kdquiz.quiz.dto.Question.QuestionGetDto;
import kdquiz.quiz.dto.Question.QuestionUpdateDto;
import kdquiz.quiz.repository.*;
import kdquiz.users.repository.UsersRepository;
import kdquiz.util.CustomFileUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
public class QuestionCRUDService {

    private final QuizRepository quizRepository;

    private final QuestionRepository questionRepository;

    private final ChoiceRepository choiceRepository;

    private final OptionRepository optionRepository;

    private final UsersRepository usersRepository;

    private final CustomFileUtil customFileUtil;

    private final QuizImgRepository quizImgRepository;

    private final QuestionSizeService questionSizeService;

    public QuestionCRUDService(QuizRepository quizRepository, QuestionRepository questionRepository, ChoiceRepository choiceRepository, OptionRepository optionRepository, UsersRepository usersRepository, CustomFileUtil customFileUtil, QuizImgRepository quizImgRepository, QuestionSizeService questionSizeService) {
        this.quizRepository = quizRepository;
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
        this.optionRepository = optionRepository;
        this.usersRepository = usersRepository;
        this.customFileUtil = customFileUtil;
        this.quizImgRepository = quizImgRepository;
        this.questionSizeService = questionSizeService;
    }

    //Question 추가
    @Transactional
    public ResponseDto<Void> questionCreate(Long quizId, Users users) {

        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try {
            // 기존 퀴즈를 찾음
            Optional<Quiz> quizOptional = quizRepository.findByEmailAndId(users.getEmail(), quizId);
            quizOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

            int size = questionSizeService.countQuestion(quizId);
            log.info("question사이즈: "+size);

            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = quizOptional.get();
            quiz.setUpdatedAt(LocalDateTime.now());
            quiz = quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음


            // 질문 엔티티 생성 및 저장
            Question question = new Question();
            question.setContent("퀴즈 문제");
            question.setCreatedAt(LocalDateTime.now());
            question.setQuiz(quiz); // 퀴즈와 연결
            question.setShortAnswer("단답형");
            question.setOrd(size);
            questionRepository.save(question);

            Options options = new Options();
            options.setUseHint(true);
            options.setHintTime(30);
            options.setHintContent("힌트ㅎㅎ");
            options.setUseAiFeedback(true);
            options.setAiQuestion("ai 피드백");
            options.setCommentary("코멘트  ㅎㅎ 피드백");
            options.setScore(20);
            options.setQuestion(question);
            optionRepository.save(options);

            for(int i=1; i<=4; i++){
                String num = Integer.toString(i+2);
                Choice choice = new Choice();
                choice.setContent(num);
                choice.setIsCorrect(false);
                choice.setShortAnswer("단답형");
                choice.setQuestion(question);
                choiceRepository.save(choice);
            }

            return ResponseDto.setSuccess("Q003", "퀴즈 업데이트 성공", null);
        } catch (Exception e) {
            return ResponseDto.setFailed("Q203","퀴즈 업데이트 실패");
        }
    }

    //Question 수정
    @Transactional
    public ResponseDto<Void> questionUpdate(Long questionId, QuestionUpdateDto questionUpdateDto,List<MultipartFile> filees, Users users) {

        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try {
            // 기존 퀴즈를 찾음
            Optional<Question> questionsOptional = questionRepository.findById(questionId);
            questionsOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

            Question questions = questionsOptional.get();
            Long QuizId = questions.getQuiz().getId();
            Optional<Quiz> quizOptional = quizRepository.findById(QuizId);
            Quiz quiz = quizOptional.get();

            Optional<Users> user = usersRepository.findByEmail(users.getEmail());
            user.orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));

            Question question = questionsOptional.get();

            question.setContent(questionUpdateDto.getContent());
            question.setUpdatedAt(LocalDateTime.now());
            question.setQuiz(quiz); // 퀴즈와 연결
            question = questionRepository.save(question); // 저장 후 ID를 얻기 위해 리턴값으로 받음

            //옵션 수정
            OptionUpdateDto optionUpdateDto = questionUpdateDto.getOptions();
            Optional<Options> optionsOptional = optionRepository.findByQuestion_Id(questionId);
            optionsOptional.orElseThrow();
            Options options = optionsOptional.get();
            options.setTime(optionUpdateDto.getTime());
            options.setUseHint(optionUpdateDto.getUseHint());
            options.setHintTime(optionUpdateDto.getHintTime());
            options.setHintContent(optionUpdateDto.getHintContent());
            options.setUseAiFeedback(optionUpdateDto.getUseAiFeedback());
            options.setAiQuestion(optionUpdateDto.getAiQuestion());
            options.setCommentary(optionUpdateDto.getCommentary());
            options.setScore(optionUpdateDto.getScore());
            options.setQuestion(question);
            optionRepository.save(options);

            for (ChoiceUpdateDto choiceUpdateDto: questionUpdateDto.getChoices()) {
                Optional<Choice> choiceOptional = choiceRepository.findById(choiceUpdateDto.getId());
                choiceOptional.orElseThrow(()->new IllegalArgumentException("선택지를 못 불러옴"));
                Choice choice = choiceOptional.get();
                choice.setContent(choiceUpdateDto.getContent());
                choice.setIsCorrect(choiceUpdateDto.getIsCorrect());
                choice.setShortAnswer(choiceUpdateDto.getShortAnswer());
                choice.setQuestion(question); // 질문과 연결
                choiceRepository.save(choice);
            }

            //기존 DB에 있는 이미지 파일 가져옴
            List<QuizImg> oldImgGet = quizImgRepository.findByQuestionId(questionId);
            List<String> oldImgList = new ArrayList<>();
            if(oldImgGet != null && !oldImgGet.isEmpty()){
                log.info("DB에 있는 이미지 조회: "+oldImgGet);
                oldImgList = oldImgGet.stream()
                        .map(QuizImg::getFileName)
                        .collect(Collectors.toList());
            }

            //기존 있는 파일 가져와서 새로운 파일과 합치기
            List<String> uploadedFileNames = questionUpdateDto.getUploadFileNames();
            if(questionUpdateDto.getFiles() != null && !questionUpdateDto.getFiles().isEmpty()){
                List<MultipartFile> files = questionUpdateDto.getFiles();
                List<String> fileNames = customFileUtil.saveFiles(files);
                if(fileNames!=null && !filees.isEmpty()){
                    uploadedFileNames.addAll(fileNames);
                }
            }

            //기존에 있는 DB와 새로운 파일 비교하여 삭제
            List<String> removesFiles = new ArrayList<>();
            if(oldImgList != null && !oldImgList.isEmpty()){
               removesFiles = oldImgList
                        .stream()
                        .filter(fileName -> uploadedFileNames.indexOf(fileName)==-1)
                        .collect(Collectors.toList());
                customFileUtil.deleteFiles(removesFiles);
                if(removesFiles!=null && !removesFiles.isEmpty()){
                    for(String removeName : removesFiles){
                       Optional<QuizImg> find = quizImgRepository.findById(removeName);
                       quizImgRepository.delete(find.get());
                    }
                }
            }

            if((removesFiles != null && !removesFiles.isEmpty()) && (uploadedFileNames != null && !uploadedFileNames.isEmpty())){
                uploadedFileNames.removeAll(removesFiles);
            }

            for(String ImgName : uploadedFileNames){
                QuizImg quizImg = new QuizImg();
                quizImg.setFileName(ImgName);
                quizImg.setQuestionId(questionId);
                quizImgRepository.save(quizImg);
            }


            return ResponseDto.setSuccess("Q003", "질문 생성 성공", null);
        } catch (Exception e) {
            return ResponseDto.setFailed("Q203","질문 생성 실패");

        }
    }

    //Question 삭제
    @Transactional
    public ResponseDto<Void> questionDelete(Long questionId, Users users) {

        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q203","사용자 정보 가져오기 실패");
        }

        try {
            // 기존 퀴즈를 찾음
            Optional<Question> questions = questionRepository.findById(questionId);
            Question question = questions.get();

            questionRepository.delete(questions.get());

            Optional<Quiz> quizOptional = quizRepository.findByEmailAndId(users.getEmail(), question.getQuiz().getId());
            quizOptional.orElseThrow(()->new IllegalArgumentException("Question를 찾을 수 없습니다."));

            List<QuizImg> quizImgList = quizImgRepository.findByQuestionId(questionId);
            if(quizImgList != null && !quizImgList.isEmpty()){
                List<String> imgList = quizImgList.stream()
                                .map(QuizImg::getFileName)
                                        .collect(Collectors.toList());
                customFileUtil.deleteFiles(imgList);
                for(String names : imgList){
                    Optional<QuizImg> quizImg = quizImgRepository.findById(names);
                    quizImgRepository.delete(quizImg.get());
                }
            }

            // 퀴즈 엔티티 생성 및 저장
            Quiz quiz = quizOptional.get();
            quiz.setUpdatedAt(LocalDateTime.now());
//            List<Question> questionList = quiz.getQuestions();
//            questionList.sort(Comparator.comparingInt(Question::getOrd));
//            for(int i=0; i<questionList.size(); i++){
//                log.info("퀘스천 정렬: "+questionList.get(i));
//                questionList.get(i).setOrd(i);
//                questionRepository.save(questionList.get(i));
//            }
            quizRepository.save(quiz); // 저장 후 ID를 얻기 위해 리턴값으로 받음

            return ResponseDto.setSuccess("Q003", "질문 삭제 성공", null);

        } catch (Exception e) {
            return ResponseDto.setFailed("Q103","질문 삭제 실패");
        }
    }

    @Transactional
    public ResponseDto<QuestionGetDto> questionGet(Long questionId, Users users){
        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try{
            Optional<Question> questionsOptional = questionRepository.findById(questionId);
            questionsOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));

            Question question = questionsOptional.get();
            // Quiz와 관련된 Questions 목록을 가져옵니다.
            QuestionGetDto questionGetDto = new QuestionGetDto();
            questionGetDto.setId(question.getId());
            questionGetDto.setContent(question.getContent());

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

            List<QuizImg> quizImgList = quizImgRepository.findByQuestionId(questionId);
            List<ImgGetDto> ImgList = new ArrayList<>();
            if(quizImgList != null && !quizImgList.isEmpty()){
                for(QuizImg quizImg : quizImgList){
                    ImgGetDto imgGetDto = new ImgGetDto();
                    imgGetDto.setFileName(quizImg.getFileName());
                    ImgList.add(imgGetDto);
                }
            }
            questionGetDto.setUploadFileNames(ImgList);
            return ResponseDto.setSuccess("Q002", "사용자가 생성한 질문 조회 성공", questionGetDto);
        } catch (Exception e) {
            return ResponseDto.setFailed("Q202", "사용자가 생성한 질문 조회 실패");
        }
    }
}
