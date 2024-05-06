package kdquiz.game.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kdquiz.ResponseDto;
import kdquiz.game.dto.GameCreateDto;
import kdquiz.game.service.GameCreateService;
import kdquiz.game.service.GameDeleteService;
import kdquiz.usersecurity.UserDetailsImpl;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class GameController {

    @Autowired
    GameCreateService gameCreateService;

    @Autowired
    GameDeleteService gameDelteService;

    @Operation(summary = "게임 핀 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "G002", description = "게임 생성 성공"),
            @ApiResponse(responseCode = "G102", description = "사용자가 없음"),
            @ApiResponse(responseCode = "G202", description = "게임 생성 실패")
    })
    @GetMapping("/game/user/{quizId}")
    public ResponseEntity<ResponseDto<GameCreateDto>> GameGet(@PathVariable Long quizId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<GameCreateDto> responseDto = gameCreateService.GameGet(quizId, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @Operation(summary = "게임 종료")
    @ApiResponses({
            @ApiResponse(responseCode = "G001", description = "게임 종료 성공"),
            @ApiResponse(responseCode = "G101", description = "게임 종료 실패")
    })
    @DeleteMapping("/game/user/{quizId}")
    public ResponseEntity<ResponseDto<Void>> GameDelete(@PathVariable Long quizId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<Void> responseDto = (ResponseDto<Void>) gameDelteService.GameDelete(quizId, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
