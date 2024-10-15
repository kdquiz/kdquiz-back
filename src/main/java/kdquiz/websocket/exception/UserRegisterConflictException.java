package kdquiz.websocket.exception;

import kdquiz.websocket.type.ExceptionCode;
import kdquiz.websocket.type.ExceptionMessage;

public class UserRegisterConflictException extends CommonException {
    public UserRegisterConflictException() {
        super(ExceptionCode.CONFLICT, ExceptionMessage.USER_REGISTER_CONFLICT);
    }
}
