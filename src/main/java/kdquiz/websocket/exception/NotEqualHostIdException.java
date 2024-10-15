package kdquiz.websocket.exception;

import kdquiz.websocket.type.ExceptionCode;
import kdquiz.websocket.type.ExceptionMessage;

public class NotEqualHostIdException extends CommonException {

    public NotEqualHostIdException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.HOST_ID_NOT_EQUAL);
    }
}
