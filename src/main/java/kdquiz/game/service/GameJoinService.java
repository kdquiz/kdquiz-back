package kdquiz.game.service;

import kdquiz.ResponseDto;
import kdquiz.domain.Participant;
import kdquiz.domain.Quiz;
import kdquiz.game.dto.GameJoinDto;
import kdquiz.game.repository.ParticipantsRepositroy;
import kdquiz.quiz.repository.QuizRepository;
import kdquiz.websocket.exception.BadRequestException;
import kdquiz.websocket.exception.NotFoundWaitRoomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GameJoinService {
    @Autowired
    QuizRepository quizRepository;

    @Autowired
    ParticipantsRepositroy participantsRepositroy;

    public ResponseDto<GameJoinDto> ParticipantsGame(GameJoinDto participantsDto) {
        try {
            int pin = participantsDto.getPin();
            String nickname = participantsDto.getNickname();
            Quiz quiz = quizRepository.findByPin(pin);
            Participant participant = new Participant();
            participant.setQuizId(quiz.getId());
            participant.setNickname(nickname);
            participantsRepositroy.save(participant);
            return ResponseDto.setSuccess("P001", "퀴즈 참여 성공", null);
        }
        catch (Exception e){
            return ResponseDto.setFailed("P101","잘못된 핀 번호");
        }
    }

    public boolean isJoinRomm(Long quizId, Long userId){
        Optional<Participant> participant = participantsRepositroy.findByQuizIdAndId(quizId, userId);
        participant.orElseThrow(NotFoundWaitRoomException::new);
        if(!participant.isEmpty()){
            return true;
        }
        throw new BadRequestException();
    }
}
