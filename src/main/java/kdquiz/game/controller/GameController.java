package kdquiz.game.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kdquiz.ResponseDto;
import kdquiz.domain.Answer;
import kdquiz.domain.Quiz;
import kdquiz.game.dto.GameCreateDto;
import kdquiz.game.dto.GameJoinDto;
import kdquiz.game.service.*;
import kdquiz.quiz.repository.QuizRepository;
import kdquiz.usersecurity.UserDetailsImpl;

import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class GameController {
//    @Autowired
//    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private GameCreateService gameCreateService;

    @Autowired
    private  GameDeleteService gameDelteService;

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    GameJoinService gameJoinService;

    @Autowired
    GamePlaySerivce gamePlaySerivce;

    @Autowired
    GameRankingService gameRankingService;

    @Operation(summary = "게임 핀 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "G002", description = "게임 생성 성공"),
            @ApiResponse(responseCode = "G102", description = "사용자가 없음"),
            @ApiResponse(responseCode = "G202", description = "게임 생성 실패")
    })
    @MessageMapping("/createRoom")
    @SendTo("/topic/rooms")
    @GetMapping("/game/{quizId}")
    public ResponseEntity<ResponseDto<GameCreateDto>> gameGet(@PathVariable Long quizId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<GameCreateDto> responseDto = gameCreateService.GameGet(quizId, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @Operation(summary = "게임 참가")
    @PostMapping("/gameJoin")
    public ResponseEntity<ResponseDto<GameJoinDto>> gameJoin(GameJoinDto gameJoinDto){
        ResponseDto<GameJoinDto> responseDto =  gameJoinService.ParticipantsGame(gameJoinDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
    @Operation(summary = "게임 종료")
    @ApiResponses({
            @ApiResponse(responseCode = "G001", description = "게임 종료 성공"),
            @ApiResponse(responseCode = "G101", description = "게임 종료 실패")
    })
    @DeleteMapping("/game/{quizId}")
    public ResponseEntity<ResponseDto<Void>> GameDelete(@PathVariable Long quizId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<Void> responseDto = (ResponseDto<Void>) gameDelteService.GameDelete(quizId, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "정답 제출")
    @ApiResponses({
            @ApiResponse(responseCode = "A001", description = "정답"),
            @ApiResponse(responseCode = "A101", description = "오답")
    })
    @PostMapping("/answer")
    public ResponseEntity<ResponseDto<Void>> Answer(@RequestParam int pin, @RequestParam long questionId, @RequestParam long playerId, @RequestParam String answer){
        ResponseDto<Void> responseDto = (ResponseDto<Void>) gamePlaySerivce.Answer(pin, questionId, playerId, answer);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/ranking")
    public ResponseEntity<ResponseDto<Void>> Ranking(@RequestParam int pin){
        ResponseDto<Void> responseDto = (ResponseDto<Void>) gameRankingService.Rank(pin);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
