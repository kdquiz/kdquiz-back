package kdquiz.game.service;

import jakarta.transaction.Transactional;
import kdquiz.ResponseDto;
import kdquiz.domain.*;
import kdquiz.game.repository.ParticipantsRepositroy;
import kdquiz.quiz.repository.ChoiceRepository;
import kdquiz.quiz.repository.OptionRepository;
import kdquiz.quiz.repository.QuestionRepository;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;

@Service
public class GamePlaySerivce {
    @Autowired
    QuizRepository quizRepository;


    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    ChoiceRepository choiceRepository;

    @Autowired
    ParticipantsRepositroy participantsRepositroy;

    @Autowired
    OptionRepository optionRepository;

    @Transactional
    public ResponseDto<?> Answer(int pin, long quiestionId, long playerId, String answer) {
        Optional<Participant> participantList = participantsRepositroy.findById(playerId);
        Participant participant = participantList.get();
        Optional<Options> optionsList = optionRepository.findByQuestion_Id(quiestionId);
        Options options = optionsList.get();
        int score = options.getScore();

        try{
            long number = Long.parseLong(answer);
            Choice choice = choiceRepository.findByIdAndQuestion_Id(number, quiestionId);
            if(choice.getIsCorrect()==true) {
                int sum = participant.getScore();
                sum += score;
                participant.setScore(sum);
                participantsRepositroy.save(participant);
                return ResponseDto.setSuccess("A001", "정답", participant);
            }else {
                return ResponseDto.setSuccess("A101", "오답", participant);
            }
        }catch (NumberFormatException e) {
            Choice choice = choiceRepository.findByShortAnswerAndQuestion_Id(answer, quiestionId);
            if(choice==null){
                return ResponseDto.setSuccess("A101", "오답", participant);
            }else{
                int sum = participant.getScore();
                sum += score;
                participant.setScore(sum);
                participantsRepositroy.save(participant);
                return ResponseDto.setSuccess("A001", "정답", participant);
            }
        }catch (Exception e){
            return ResponseDto.setSuccess("A101", "오답", participant);
        }




    }
}
