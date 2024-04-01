package kdquiz.quiz.controller;

import kdquiz.quiz.dto.GetQuizDto;
import kdquiz.quiz.dto.QuizCreateDto;
import kdquiz.quiz.dto.QuizUpdateDto;
import kdquiz.quiz.exception.ResponseDto;
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


    @PostMapping("/quiz")
    public ResponseEntity<ResponseDto<Void>> createQuiz(@RequestBody QuizCreateDto quizCreateDto) {
        ResponseDto<Void> responseDto = (ResponseDto<Void>) quizCreateService.createQuiz(quizCreateDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @GetMapping("/quiz/user/{userId}")
    public ResponseEntity<ResponseDto<List<GetQuizDto>>> GetQuiz(@PathVariable Long userId){
        ResponseDto<List<GetQuizDto>> responseDto = getQuizService.GetQuiz(userId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/quiz/{quizId}")
    public ResponseEntity<ResponseDto<Void>> QuizUpdate(@PathVariable Long quizId, @RequestBody QuizUpdateDto quizUpdateDto){
        ResponseDto<Void> responseDto = quizUpdateService.QuizUpdate(quizId, quizUpdateDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/quiz/{quizId}")
    public ResponseEntity<ResponseDto<Void>> QuizDelete(@PathVariable Long quizId){
        ResponseDto<Void> responseDto = (ResponseDto<Void>) quizDeleteService.QuizDelete(quizId);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
