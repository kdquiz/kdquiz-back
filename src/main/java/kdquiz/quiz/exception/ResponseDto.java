package kdquiz.quiz.exception;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Setter
@Getter
public class ResponseDto<T> implements Serializable {
    private String code;
    private int status;
    private String message;
    private T data;
}
