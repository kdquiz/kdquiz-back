package kdquiz.quiz.service;

import kdquiz.domain.*;
import kdquiz.quiz.dto.*;
import kdquiz.ResponseDto;
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

    @Transactional
    public ResponseDto<Void> QuizUpdate(Long quizId, QuizUpdateDto quizUpdateeDto, MultipartFile[] files, Users users) {

        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try {
            // 기존 퀴즈를 찾음
            Optional<Quiz> quizOptional = quizRepository.findByIdAndEmail(quizId, users.getEmail());
            quizOptional.orElseThrow(()->new IllegalArgumentException("퀴즈를 찾을 수 없습니다."));
            Optional<Users> user = usersRepository.findByEmail(users.getEmail());
            Users userId  = user.get();

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

            int i=0;
            // 질문 및 선택 생성 및 연결
            for (QuestionUpdateDto questionUpdateDto : quizUpdateeDto.getQuestions()) {
                Optional<Questions> questionsOptional = questionRepository.findById(questionUpdateDto.getId());
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
