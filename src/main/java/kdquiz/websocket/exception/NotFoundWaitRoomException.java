package kdquiz.websocket.exception;

import kdquiz.websocket.type.ExceptionCode;
import kdquiz.websocket.type.ExceptionMessage;

public class NotFoundWaitRoomException extends CommonException {
    public NotFoundWaitRoomException() {
        super(ExceptionCode.NOT_FOUND, ExceptionMessage.WAITROOM_NOT_FOUND);
    }
}
