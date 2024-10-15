package kdquiz.websocket.exception;

import kdquiz.websocket.type.ExceptionCode;
import kdquiz.websocket.type.ExceptionMessage;

public class BindingInvalidException extends CommonException {
    public BindingInvalidException() {
        super(ExceptionCode.BAD_REQUEST, ExceptionMessage.BINDING_INVALID);
    }
}
