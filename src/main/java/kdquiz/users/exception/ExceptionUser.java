package kdquiz.users.exception;

import kdquiz.ResponseDto;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@ControllerAdvice
@RestController
public class ExceptionUser {
    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseDto<?> exceptionHandler(Exception exception){
        String message = exception.getMessage();
        return ResponseDto.setFailed(message);
    }
}
