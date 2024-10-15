package kdquiz.websocket.exception;


import kdquiz.websocket.type.ExceptionCode;
import kdquiz.websocket.type.ExceptionMessage;

public class NotFoundUserException extends CommonException {

    public NotFoundUserException() {super(ExceptionCode.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND);}
}
