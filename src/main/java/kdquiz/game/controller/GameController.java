package kdquiz.game.controller;


import kdquiz.ResponseDto;
import kdquiz.game.dto.GameCreateDto;
import kdquiz.game.service.GameCreateService;
import kdquiz.quiz.dto.QuizGetDto;
import kdquiz.quiz.service.QuizGetService;
import kdquiz.usersecurity.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class GameController {

    @Autowired
    GameCreateService gameCreateService;

    @GetMapping("/game/user/{quizId}")
    public ResponseEntity<ResponseDto<GameCreateDto>> GameGet(@PathVariable Long quizId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<GameCreateDto> responseDto = gameCreateService.GameGet(quizId, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }
}
