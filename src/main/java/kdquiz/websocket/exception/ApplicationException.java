package kdquiz.websocket.exception;

import lombok.Getter;
import org.springframework.validation.BindingResult;

@Getter
public class ApplicationException extends RuntimeException {

    private final String errorCode;
    private BindingResult errors;

    protected ApplicationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected ApplicationException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    protected ApplicationException(String errorCode, String message, BindingResult errors) {
        super(message);
        this.errorCode = errorCode;
        this.errors = errors;
    }

    protected ApplicationException(String errorCode, String message, BindingResult errors, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.errors = errors;
    }


}
