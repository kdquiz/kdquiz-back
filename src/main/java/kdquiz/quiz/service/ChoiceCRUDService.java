package kdquiz.quiz.service;

import kdquiz.ResponseDto;
import kdquiz.domain.Choice;
import kdquiz.domain.Question;
import kdquiz.domain.Users;
import kdquiz.quiz.dto.Choice.ChoiceGetDto;
import kdquiz.quiz.dto.Choice.ChoiceListUpdate;
import kdquiz.quiz.dto.Choice.ChoiceUpdateDto;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.QuestionRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ChoiceCRUDService {

    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;

    public ChoiceCRUDService(QuestionRepository questionRepository, ChoiceRepository choiceRepository) {
        this.questionRepository = questionRepository;
        this.choiceRepository = choiceRepository;
    }

    @Transactional
    public ResponseDto<ChoiceGetDto> choiceCreate(Long questionId, Users users){
        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try{
            Optional<Question> questionOptional = questionRepository.findById(questionId);
            questionOptional.orElseThrow(()->new IllegalArgumentException("Question 찾을 수 없습니다."));

            Question question = questionOptional.get();
            Choice choice = new Choice();
            choice.setContent("문제 입력");
            choice.setIsCorrect(false);
            choice.setShortAnswer("");
            choice.setQuestion(question);

            // Choice를 Question의 choice 리스트에 추가
            List<Choice> choiceList = question.getChoice();
            choiceList.add(choice);
            question.setChoice(choiceList);

            ChoiceGetDto choiceGetDto = new ChoiceGetDto();
            choiceGetDto.setContent(choice.getContent());
            choiceGetDto.setShortAnswer(choice.getShortAnswer());
            choiceGetDto.setIsCorrect(choice.getIsCorrect());
            // 두 개를 모두 저장하여 관계를 명확히 함
            questionRepository.save(question);
            choiceRepository.save(choice);


            return ResponseDto.setSuccess("Q003", "choice 추가", choiceGetDto);
        }catch (Exception e) {
            return ResponseDto.setFailed("Q203","choice 추가 실패");
        }
    }

    @Transactional
    public ResponseDto<Void> choiceDelete(Long choiceId, Users users){
        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try{
            Optional<Choice> choiceOptional = choiceRepository.findById(choiceId);
            choiceOptional.orElseThrow(()-> new IllegalArgumentException("choice 찾기 실패"));
            choiceRepository.delete(choiceOptional.get());
            return ResponseDto.setSuccess("Q003", "choice 삭제 성공", null);
        }catch (Exception e){
            return ResponseDto.setFailed("Q203","choice 삭제 실패");
        }
    }

    @Transactional
    public ResponseDto<List<ChoiceGetDto>> choiceGet(Long questionId, Users users){
        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try{
            Optional<Question> questionOptional = questionRepository.findById(questionId);
            questionOptional.orElseThrow(()->new IllegalArgumentException("choice 조회 실패"));

            List<Choice> choiceList = questionOptional.get().getChoice();
            List<ChoiceGetDto> choiceGetList = new ArrayList<>();
            for(int i=0; i<choiceList.size(); i++){
                ChoiceGetDto choiceGetDto = new ChoiceGetDto();
                choiceGetDto.setId(choiceList.get(i).getId());
                choiceGetDto.setContent(choiceList.get(i).getContent());
                choiceGetDto.setIsCorrect(choiceList.get(i).getIsCorrect());
                choiceGetDto.setShortAnswer(choiceList.get(i).getShortAnswer());
                choiceGetList.add(choiceGetDto);
            }
            return ResponseDto.setSuccess("Q003", "choice 조회 성공", choiceGetList);
        }catch (Exception e){
            return ResponseDto.setFailed("Q203","choice 조회 실패");
        }
    }


    @Transactional
    public ResponseDto<List<ChoiceGetDto>> choiceAllUpdate(Long questionId, ChoiceListUpdate choiceListUpdate, Users users){
        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try{
            Optional<Question> questionOptional = questionRepository.findById(questionId);
            questionOptional.orElseThrow(()-> new IllegalArgumentException("question 찾기 실패"));
            List<Choice> choiceList = questionOptional.get().getChoice();
            List<ChoiceGetDto> choiceGetDtos = new ArrayList<>();

            for(ChoiceUpdateDto dto : choiceListUpdate.getChoices()){
                ChoiceGetDto choiceGetDto = new ChoiceGetDto();
                Choice choiceUpdate = choiceList.stream()
                        .filter(choice -> choice.getId().equals(dto.getId()))
                        .findFirst()
                        .orElseThrow(()->new IllegalArgumentException("choice 찾기 실패"));
                choiceUpdate.setContent(dto.getContent());
                choiceUpdate.setShortAnswer(dto.getShortAnswer());
                choiceUpdate.setIsCorrect(dto.getIsCorrect());

                choiceGetDto.setId(dto.getId());
                choiceGetDto.setContent(dto.getContent());
                choiceGetDto.setShortAnswer(dto.getShortAnswer());
                choiceGetDto.setIsCorrect(dto.getIsCorrect());
                choiceGetDtos.add(choiceGetDto);
            }
            choiceRepository.saveAll(choiceList);
            return ResponseDto.setSuccess("Q003", "choice 수정 성공", choiceGetDtos);
        } catch (Exception e){
            return ResponseDto.setFailed("Q203","choice 수정 실패");
        }
    }

    @Transactional
    public ResponseDto<ChoiceGetDto> choiceUpdate(ChoiceUpdateDto choiceUpdateDto, Users users){
        if(users.getEmail()==null){
            return ResponseDto.setFailed("Q403","사용자 정보 가져오기 실패");
        }
        try{
            Optional<Choice> choiceOptional = choiceRepository.findById(choiceUpdateDto.getId());
            choiceOptional.orElseThrow(()->new IllegalArgumentException("choice 못 찾음"));
            Choice choice = choiceOptional.get();
            choice.setContent(choiceUpdateDto.getContent());
            choice.setIsCorrect(choiceUpdateDto.getIsCorrect());
            choice.setShortAnswer(choiceUpdateDto.getShortAnswer());
            choiceRepository.save(choice);

            ChoiceGetDto choiceGetDto = new ChoiceGetDto();
            choiceGetDto.setId(choiceUpdateDto.getId());
            choiceGetDto.setContent(choiceUpdateDto.getContent());
            choiceGetDto.setShortAnswer(choiceUpdateDto.getShortAnswer());
            choiceGetDto.setIsCorrect(choiceUpdateDto.getIsCorrect());

            return ResponseDto.setSuccess("Q003", "choice 수정 성공", choiceGetDto);
        }catch (Exception e){
            return ResponseDto.setFailed("Q203","choice 수정 실패");
        }
    }
}
