package kdquiz.participants.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import kdquiz.ResponseDto;
import kdquiz.game.dto.GameCreateDto;
import kdquiz.participants.dto.ParticipantsDto;
import kdquiz.participants.service.CreateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ParticipantsController {
    @Autowired
    CreateService createService;

    @Operation(summary = "게임 참여")
    @ApiResponses({
            @ApiResponse(responseCode = "P001", description = "게임 참여 성공"),
            @ApiResponse(responseCode = "P101", description = "잘못된 핀 번호")
    })
    @PostMapping("/game/participants")
    public ResponseEntity<ResponseDto<Void>> Participation(@RequestBody @Valid ParticipantsDto participantsDto) {
        ResponseDto<Void> responseDto  = (ResponseDto<Void>) createService.ParticipantsGame(participantsDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }
}
