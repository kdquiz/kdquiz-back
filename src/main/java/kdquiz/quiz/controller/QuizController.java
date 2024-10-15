package kdquiz.quiz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import kdquiz.ResponseDto;
import kdquiz.quiz.dto.Question.QuestionGetDto;
import kdquiz.quiz.dto.Question.QuestionUpdateDto;
import kdquiz.quiz.dto.Quiz.QuizCreateDto;
import kdquiz.quiz.dto.Quiz.QuizGetAllDto;
import kdquiz.quiz.dto.Quiz.QuizGetDto;
import kdquiz.quiz.service.*;
import kdquiz.usersecurity.UserDetailsImpl;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Log4j2
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

    @Autowired
    QuestionCRUDService questionCRUDService;


//    @Operation(summary = "퀴즈생성 토큰 헤더에 넣으셈")
//    @ApiResponses({
//            @ApiResponse(responseCode = "Q001", description = "퀴즈 생성 성공"),
//            @ApiResponse(responseCode = "Q101", description = "퀴즈 생성 실패"),
//            @ApiResponse(responseCode = "Q201", description = "존재하지 않는 회원")
//    })
//    @PostMapping("/quiz")
//    public ResponseEntity<ResponseDto<Void>> createQuiz(@RequestPart QuizCreateDto quizCreateDto, @RequestPart(value = "files", required = false)MultipartFile[] files, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        System.out.println("files : "+files);
//        ResponseDto<Void> responseDto = (ResponseDto<Void>) quizCreateService.createQuiz(quizCreateDto, files, userDetails.getUsers());
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }

    @Operation(summary = "퀴즈생성 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q001", description = "퀴즈 생성 성공"),
            @ApiResponse(responseCode = "Q101", description = "퀴즈 생성 실패"),
            @ApiResponse(responseCode = "Q201", description = "존재하지 않는 회원")
    })
    @PostMapping("/quiz")
    public ResponseEntity<ResponseDto<?>> CreateQuiz(@RequestBody QuizCreateDto quizCreateDto, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<Void> responseDto = (ResponseDto<Void>) quizCreateService.createQuiz(quizCreateDto, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

//    @Operation(summary = "퀴즈 수정 토큰 헤더에 넣으셈")
//    @ApiResponses({
//            @ApiResponse(responseCode = "Q003", description = "퀴즈 수정 성공"),
//            @ApiResponse(responseCode = "Q103", description = "퀴즈를 찾을 수 없음"),
//            @ApiResponse(responseCode = "Q203", description = "퀴즈 수정 실패"),
//            @ApiResponse(responseCode = "Q303", description = "사용자 못 찾음")
//    })
//    @PutMapping("/quiz/{quizId}")
//    public ResponseEntity<ResponseDto<Void>> QuizUpdate(@PathVariable Long quizId, @RequestPart QuizUpdateDto quizUpdateDto, @RequestPart(value = "files", required = false)MultipartFile[] files, @AuthenticationPrincipal UserDetailsImpl userDetails){
//        ResponseDto<Void> responseDto = quizUpdateService.QuizUpdate(quizId, quizUpdateDto, files, userDetails.getUsers());
//        return new ResponseEntity<>(responseDto, HttpStatus.OK);
//    }


    @Operation(summary = "사용자가 생성한 모든 퀴즈조회 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q002", description = "퀴즈 조회 성공"),
            @ApiResponse(responseCode = "Q102", description = "사용자가 없음"),
            @ApiResponse(responseCode = "Q202", description = "사용자가 생성한 퀴즈 목록 조회 실패")
    })
    @GetMapping("/quiz")
    public ResponseEntity<ResponseDto<List<QuizGetAllDto>>> QuizGetAll(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(required = false, defaultValue = "Time_desc") String SortBy, @RequestParam(required = false) String searchTitle){
        ResponseDto<List<QuizGetAllDto>> responseDto = quizGetService.quizGetAll(userDetails.getUsers(), SortBy, searchTitle);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "사용자가 생성한 개별 퀴즈조회 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q002", description = "퀴즈 조회 성공"),
            @ApiResponse(responseCode = "Q102", description = "사용자가 없음"),
            @ApiResponse(responseCode = "Q202", description = "사용자가 생성한 퀴즈 조회 실패")
    })
    @GetMapping("/quiz/{quizId}")
    public ResponseEntity<ResponseDto<QuizGetDto>> QuizGet(@PathVariable Long quizId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<QuizGetDto> responseDto = quizGetService.quizGet(quizId, userDetails.getUsers());
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
    public ResponseEntity<ResponseDto<Void>> QuizDelete(@PathVariable Long quizId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<Void> responseDto = (ResponseDto<Void>) quizDeleteService.QuizDelete(quizId, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    //Question 생성
    @Operation(summary = "Question 생성 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q003", description = "질문 생성 성공"),
            @ApiResponse(responseCode = "Q103", description = "퀴즈를 찾을 수 없음"),
            @ApiResponse(responseCode = "Q203", description = "질문 생성 실패"),
            @ApiResponse(responseCode = "Q303", description = "사용자 못 찾음")
    })
    @PostMapping("/question/{quizId}")
    public ResponseEntity<ResponseDto<Void>> QuestionCreateDto(@PathVariable Long quizId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<Void> responseDto = questionCRUDService.questionCreate(quizId, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


    @Operation(summary = "Question 수정 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q003", description = "퀴즈 수정 성공"),
            @ApiResponse(responseCode = "Q103", description = "퀴즈를 찾을 수 없음"),
            @ApiResponse(responseCode = "Q203", description = "퀴즈 수정 실패"),
            @ApiResponse(responseCode = "Q303", description = "사용자 못 찾음")
    })
    @PutMapping("/question/{questionId}")
    public ResponseEntity<ResponseDto<Void>> QuestionUpdate(@PathVariable Long questionId, @RequestPart("question") QuestionUpdateDto questionUpdateDto,
                                                            @RequestPart(value = "files", required = false) List<MultipartFile> files, @AuthenticationPrincipal UserDetailsImpl userDetails){
        if(files != null){
            questionUpdateDto.setFiles(files);
        }
        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                log.info("파일");
                log.info(file);
            }
        } else {
            log.info("No files were uploaded.");
        }
        log.info("받아온 데이터");
        log.info(questionUpdateDto);
        ResponseDto<Void> responseDto = questionCRUDService.questionUpdate(questionId, questionUpdateDto, files ,userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "Question 질문조회 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q003", description = "퀴즈 수정 성공"),
            @ApiResponse(responseCode = "Q103", description = "퀴즈를 찾을 수 없음"),
            @ApiResponse(responseCode = "Q203", description = "퀴즈 수정 실패"),
            @ApiResponse(responseCode = "Q303", description = "사용자 못 찾음")
    })
    @GetMapping("/question/{questionId}")
    public ResponseEntity<ResponseDto<QuestionGetDto>> QuestionGet(@PathVariable Long questionId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<QuestionGetDto> responseDto = questionCRUDService.questionGet(questionId, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @Operation(summary = "Question 삭제 토큰 헤더에 넣으셈")
    @ApiResponses({
            @ApiResponse(responseCode = "Q003", description = "질문 삭제 성공"),
            @ApiResponse(responseCode = "Q103", description = "질문 삭제 실패"),
            @ApiResponse(responseCode = "Q203", description = "사용자 못 찾음")
    })
    @DeleteMapping("/question/{questionId}")
    public ResponseEntity<ResponseDto<Void>> QuestionDelete(@PathVariable Long questionId, @AuthenticationPrincipal UserDetailsImpl userDetails){
        ResponseDto<Void> responseDto = questionCRUDService.questionDelete(questionId, userDetails.getUsers());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }


}
