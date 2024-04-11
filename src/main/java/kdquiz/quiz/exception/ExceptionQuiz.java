package kdquiz.quiz.exception;

import kdquiz.ResponseDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionQuiz {
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseDto<?> exceptionHandler(Exception exception){
        String message = exception.getMessage();
        return ResponseDto.setFailedMessage(message);
    }
}
