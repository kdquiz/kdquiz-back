package kdquiz.websocket.exception;

import kdquiz.websocket.type.ExceptionCode;
import kdquiz.websocket.type.ExceptionMessage;

public class RedisLockException extends CommonException {

    public RedisLockException() {
        super(ExceptionCode.CONFLICT, ExceptionMessage.REDIS_ROCK_EXCEPTION);
    }
}
