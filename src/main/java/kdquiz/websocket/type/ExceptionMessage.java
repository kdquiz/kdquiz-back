package kdquiz.websocket.type;

public class ExceptionMessage {
    public static final String USER_NOT_FOUND = "존재하지 않는 회원입니다.";
    public static final String USER_NOT_REFRESHTOKEN = "존재하지 않는 refreshToken 입니다,";
    public static final String USER_REGISTER_CONFLICT = "이미 존재하는 회원힙니다.";
    public static final String BINDING_INVALID = "유효하지 않은 요청입니다.";
    public static final String BAD_REQUEST = "유효하지 않은 요청입니다.";
    public static final String INTERNAL_SERVER_ERROR = "일시적인 서버 에러입니다.";
    public static final String WAITROOM_NOT_FOUND = "존재하지 않는 대기방입니다.";
    public static final String HOST_ID_NOT_EQUAL = "요청한 정보가 일치하지 않습니다.";
    public static final String NOT_SATISFIED_MIN_JOIN_MEMBERS = "게임 가능한 최소 인원을 만족하지 못하였습니다.";
    public static final String REDIS_ROCK_EXCEPTION = "다른 요청이 처리 중입니다.";

}
