package kdquiz.websocket.exception;


import kdquiz.websocket.type.ExceptionCode;
import kdquiz.websocket.type.ExceptionMessage;

public class BadJwtRequestException extends CommonException {

    public BadJwtRequestException() {super(ExceptionCode.BAD_REQUEST, ExceptionMessage.BAD_REQUEST);}
}
