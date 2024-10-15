package kdquiz.websocket.exception;

import kdquiz.websocket.type.ExceptionCode;
import kdquiz.websocket.type.ExceptionMessage;

public class WebsocketSecurityException extends CommonException {

    public WebsocketSecurityException() {
        super(ExceptionCode.BAD_REQUEST, ExceptionMessage.BAD_REQUEST);
    }
}
