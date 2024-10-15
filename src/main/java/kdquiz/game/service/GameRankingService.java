package kdquiz.game.service;

import jakarta.transaction.Transactional;
import kdquiz.ResponseDto;
import kdquiz.domain.Participant;
import kdquiz.domain.Quiz;
import kdquiz.game.dto.RankDto;
import kdquiz.game.repository.ParticipantsRepositroy;
import kdquiz.quiz.repository.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GameRankingService {
    @Autowired
    ParticipantsRepositroy participantsRepositroy;

    @Autowired
    QuizRepository quizRepository;

    @Transactional
    public ResponseDto<?> Rank(int pin) {
        try{
            Quiz quizId = quizRepository.findByPin(pin);
            List<Participant> rankList = participantsRepositroy.findByQuizIdOrderByScoreDesc(quizId.getId());

            int ranks = 1;
            for(Participant participant : rankList){
                participant.setRanking(ranks);
                ranks++;
                participantsRepositroy.save(participant);
            }


            List<RankDto> rankDtoList = new ArrayList<>();
            for(Participant participantList : rankList){
                RankDto rankDtos = new RankDto();
                rankDtos.setId(participantList.getId());
                rankDtos.setScore(participantList.getScore());
                rankDtos.setRank(participantList.getRanking());
                rankDtoList.add(rankDtos);
            }
            return ResponseDto.setSuccess("R001", "순위정보", rankDtoList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed("R101", "순위정보 실패");
        }
    }
}
