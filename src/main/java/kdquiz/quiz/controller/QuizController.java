package kdquiz.quiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kdquiz.quiz.dto.QuizCreateDto;

import kdquiz.quiz.dto.QuizGetAllDto;
import kdquiz.quiz.dto.QuizGetDto;
import kdquiz.quiz.dto.QuizUpdateDto;
import kdquiz.ResponseDto;
import kdquiz.quiz.service.QuizCreateService;
import kdquiz.quiz.service.QuizDeleteService;
import kdquiz.quiz.service.QuizGetService;
import kdquiz.quiz.service.QuizUpdateService;
import kdquiz.usersecurity.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    QuizGetService quizGetService;


    @Operation(summary = "퀴즈생성 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q001", description = "퀴즈 생성 성공"),
            @ApiResponse(responseCode = "Q101", description = "퀴즈 생성 실패"),
            @ApiResponse(responseCode = "Q201", description = "존재하지 않는 회원")
    })
    @PostMapping("/quiz")
//    @PreAuthorize("hasRole('user')")
    public ResponseEntity<ResponseDto<Void>> createQuiz(@RequestBody QuizCreateDto quizCreateDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        ResponseDto<Void> responseDto = (ResponseDto<Void>) quizCreateService.createQuiz(quizCreateDto, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "사용자가 생성한 모든 퀴즈조회 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q002", description = "퀴즈 조회 성공"),
            @ApiResponse(responseCode = "Q102", description = "사용자가 없음"),
            @ApiResponse(responseCode = "Q202", description = "사용자가 생성한 퀴즈 목록 조회 실패")
    })
    @GetMapping("/quiz/user")
//    @PreAuthorize("hasRole('user')")
    public ResponseEntity<ResponseDto<List<QuizGetAllDto>>> QuizGetAll(@AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<List<QuizGetAllDto>> responseDto = quizGetService.QuizGetAll(userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "사용자가 생성한 개별 퀴즈조회 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q002", description = "퀴즈 조회 성공"),
            @ApiResponse(responseCode = "Q102", description = "사용자가 없음"),
            @ApiResponse(responseCode = "Q202", description = "사용자가 생성한 퀴즈 조회 실패")
    })
    @GetMapping("/quiz/user/{quizId}")
    public ResponseEntity<ResponseDto<QuizGetDto>> QuizGet(@PathVariable Long quizId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<QuizGetDto> responseDto = quizGetService.QuizGet(quizId, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);

    }

    @Operation(summary = "퀴즈 수정 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q003", description = "퀴즈 수정 성공"),
            @ApiResponse(responseCode = "Q103", description = "퀴즈를 찾을 수 없음"),
            @ApiResponse(responseCode = "Q203", description = "퀴즈 수정 실패"),
            @ApiResponse(responseCode = "Q303", description = "사용자 못 찾음")
    })
    @PutMapping("/quiz/{quizId}")
//   @PreAuthorize("hasRole('user')")
    public ResponseEntity<ResponseDto<Void>> QuizUpdate(@PathVariable Long quizId, @RequestBody QuizUpdateDto quizUpdateDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<Void> responseDto = quizUpdateService.QuizUpdate(quizId, quizUpdateDto, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "퀴즈삭제 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q004", description = "퀴즈 삭제 성공"),
            @ApiResponse(responseCode = "Q102", description = "삭제 할 퀴즈가 없음"),
            @ApiResponse(responseCode = "Q204", description = "퀴즈 삭제 실패"),
            @ApiResponse(responseCode = "Q304", description = "사용자 못 찾음")
    })
    @DeleteMapping("/quiz/{quizId}")
//    @PreAuthorize("hasRole('user')")
    public ResponseEntity<ResponseDto<Void>> QuizDelete(@PathVariable Long quizId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<Void> responseDto = (ResponseDto<Void>) quizDeleteService.QuizDelete(quizId, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
