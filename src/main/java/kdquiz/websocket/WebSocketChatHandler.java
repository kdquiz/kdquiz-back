package kdquiz.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper mapper;

    // 현재 연결된 세션들
    private final Set<WebSocketSession> sessions = new HashSet<>();

    // chatRoomId: {session1, session2}
    private final Map<Long,Set<WebSocketSession>> chatRoomSessionMap = new HashMap<>();

    private static final ConcurrentHashMap<String, WebSocketSession> CLIENTS = new ConcurrentHashMap<String, WebSocketSession>();
    // 소켓 연결 확인
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // TODO Auto-generated method stub
        CLIENTS.put(session.getId(), session);
        log.info("{} 연결됨", session.getId());
        log.info("{} 연결됨", session.getUri());

        sessions.add(session);

    }



    // 소켓 통신 시 메세지의 전송을 다루는 부분
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String id = session.getId();
        String[] pathArr = Objects.requireNonNull(session.getUri()).getPath().split("/");
        String pin = pathArr[pathArr.length-1];
        String payload = message.getPayload();

        CLIENTS.entrySet().forEach( arg->{
            String[] cpathArr = Objects.requireNonNull(arg.getValue().getUri()).getPath().split("/");
            String cpin = cpathArr[cpathArr.length-1];
            if(Objects.equals(pin, cpin)&&!arg.getKey().equals(id)) {  //같은 아이디가 아니면 메시지를 전달합니다.
                try {
                    arg.getValue().sendMessage(message);
                    log.info("payload {}", pin);  // 핀번호
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        log.info("payload {}", payload);

        // 페이로드 -> chatMessageDto로 변환


    }



    // 소켓 종료 확인
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // TODO Auto-generated method stub
        log.info("{} 연결 끊김", session.getId());
        sessions.remove(session);
        CLIENTS.remove(session.getId());
    }

    // ====== 채팅 관련 메소드 ======
    private void removeClosedSession(Set<WebSocketSession> chatRoomSession) {
        chatRoomSession.removeIf(sess -> !sessions.contains(sess));
    }


    public <T> void sendMessage(WebSocketSession session, T message) {
        try{
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
