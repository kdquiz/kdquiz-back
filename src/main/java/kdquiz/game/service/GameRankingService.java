package kdquiz.game.service;

import jakarta.transaction.Transactional;
import kdquiz.ResponseDto;
import kdquiz.domain.Participants;
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
            List<Participants> rankList = participantsRepositroy.findByQuizIdOrderByScoreDesc(quizId.getId());

            int ranks = 1;
            for(Participants participants : rankList){
                participants.setRanking(ranks);
                ranks++;
                participantsRepositroy.save(participants);
            }


            List<RankDto> rankDtoList = new ArrayList<>();
            for(Participants participantsList : rankList){
                RankDto rankDtos = new RankDto();
                rankDtos.setId(participantsList.getId());
                rankDtos.setScore(participantsList.getScore());
                rankDtos.setRank(participantsList.getRanking());
                rankDtoList.add(rankDtos);
            }
            return ResponseDto.setSuccess("R001", "순위정보", rankDtoList);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseDto.setFailed("R101", "순위정보 실패");
        }
    }
}
