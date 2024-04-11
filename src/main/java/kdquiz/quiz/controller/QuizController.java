package kdquiz.quiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kdquiz.quiz.dto.QuizGetDto;
import kdquiz.quiz.dto.QuizCreateDto;
import kdquiz.quiz.dto.QuizUpdateDto;
import kdquiz.ResponseDto;
import kdquiz.quiz.service.QuizCreateService;
import kdquiz.quiz.service.QuizDeleteService;
import kdquiz.quiz.service.QuizGetService;
import kdquiz.quiz.service.QuizUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class QuizController {

    @Autowired
    QuizCreateService quizCreateService;

    @Autowired
    QuizDeleteService quizDeleteService;

    @Autowired
    QuizUpdateService quizUpdateService;

    @Autowired
    QuizGetService getQuizService;

    @Operation(summary = "퀴즈생성")
    @ApiResponses({
            @ApiResponse(responseCode = "Q001", description = "퀴즈 생성 성공"),
            @ApiResponse(responseCode = "Q101", description = "퀴즈 생성 실패")
    })
    @PostMapping("/quiz")
    public ResponseEntity<ResponseDto<Void>> createQuiz(@RequestBody QuizCreateDto quizCreateDto) {
        ResponseDto<Void> responseDto = (ResponseDto<Void>) quizCreateService.createQuiz(quizCreateDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "퀴즈조회")
    @ApiResponses({
            @ApiResponse(responseCode = "Q002", description = "퀴즈 조회 성공"),
            @ApiResponse(responseCode = "Q102", description = "사용자가 없음"),
            @ApiResponse(responseCode = "Q202", description = "사용자가 생성한 퀴즈 목록 조회 실패")
    })
    @GetMapping("/quiz/user/{userId}")
    public ResponseEntity<ResponseDto<List<QuizGetDto>>> GetQuiz(@PathVariable Long userId){
        ResponseDto<List<QuizGetDto>> responseDto = getQuizService.GetQuiz(userId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "퀴즈 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "Q003", description = "퀴즈 수정 성공"),
            @ApiResponse(responseCode = "Q103", description = "퀴즈를 찾을 수 없음"),
            @ApiResponse(responseCode = "Q203", description = "퀴즈 수정 실패")
    })
    @PutMapping("/quiz/{quizId}")
    public ResponseEntity<ResponseDto<Void>> QuizUpdate(@PathVariable Long quizId, @RequestBody QuizUpdateDto quizUpdateDto){
        ResponseDto<Void> responseDto = quizUpdateService.QuizUpdate(quizId, quizUpdateDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "퀴즈삭제")
    @ApiResponses({
            @ApiResponse(responseCode = "Q004", description = "퀴즈 삭제 성공"),
            @ApiResponse(responseCode = "Q102", description = "삭제 할 퀴즈가 없음"),
            @ApiResponse(responseCode = "Q204", description = "퀴즈 삭제 실패")
    })
    @DeleteMapping("/quiz/{quizId}")
    public ResponseEntity<ResponseDto<Void>> QuizDelete(@PathVariable Long quizId){
        ResponseDto<Void> responseDto = (ResponseDto<Void>) quizDeleteService.QuizDelete(quizId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
