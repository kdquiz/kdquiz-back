package kdquiz.websocket.exception;

import kdquiz.websocket.type.ExceptionCode;
import kdquiz.websocket.type.ExceptionMessage;

public class NotSatisfiedMinJoinMembers extends CommonException {
    public NotSatisfiedMinJoinMembers() {super(ExceptionCode.BAD_REQUEST, ExceptionMessage.NOT_SATISFIED_MIN_JOIN_MEMBERS);}
}
