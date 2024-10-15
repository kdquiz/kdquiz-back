package kdquiz.websocket.interceptor;

import kdquiz.ResponseDto;
import kdquiz.game.service.GameJoinService;
import kdquiz.websocket.exception.WebsocketSecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.support.WebExchangeBindException;

@Log4j2
@RequiredArgsConstructor
@Component
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    private final GameJoinService gameJoinService;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.CONNECT.equals(headerAccessor.getCommand())){
            isValidateWaitRoomIdAndJoinMember(headerAccessor);
        }
        return message;
    }

    private void isValidateWaitRoomIdAndJoinMember(StompHeaderAccessor headerAccessor){
        Long userId = Long.valueOf(headerAccessor.getFirstNativeHeader("UserId"));
        Long quizId = Long.valueOf(headerAccessor.getFirstNativeHeader("QuizId"));
        if(userId ==null || quizId==null){
            throw new WebsocketSecurityException();
        }
        String destination = headerAccessor.getDestination();
        if(destination==null){throw new WebsocketSecurityException();}

        if(isApplyUrl(destination)){
            gameJoinService.isJoinRomm(quizId,userId);
        }
    }

    private boolean isApplyUrl(String destination){
        return !antPathMatcher.match("/wait-service/waitroom/**/**/join", destination);
    }

}
