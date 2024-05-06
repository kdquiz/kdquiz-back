package kdquiz.participants.service;

import kdquiz.ResponseDto;
import kdquiz.domain.Participants;
import kdquiz.domain.Quiz;
import kdquiz.participants.dto.ParticipantsDto;
import kdquiz.participants.repository.ParticipantsRepositroy;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.stereotype.Service;

@Service
public class CreateService {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    ParticipantsRepositroy participantsRepositroy;

    public ResponseDto<?> ParticipantsGame(ParticipantsDto participantsDto) {
        try {
            int pin = participantsDto.getPin();
            String nickname = participantsDto.getNickname();
            Quiz quiz = quizRepository.findByPin(pin);
            Long quizId = quiz.getId();
            Participants participants = new Participants();
            participants.setQuizId(quiz.getId());
            participants.setNickname(nickname);
            participantsRepositroy.save(participants);
            return ResponseDto.setSuccess("P001", "퀴즈 참여 성공", participants);
        }
        catch (Exception e){
            return ResponseDto.setFailed("P101","잘못된 핀 번호");
        }
    }
}
