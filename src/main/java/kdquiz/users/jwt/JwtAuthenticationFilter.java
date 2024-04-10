//package kdquiz.jwt;
//
//import io.jsonwebtoken.Claims;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import kdquiz.users.domain.Users;
//import kdquiz.users.repository.UsersRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.authentication.AbstractAuthenticationToken;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.context.SecurityContext;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Optional;
//이거 사용 안하는 거임
//@Component
//@RequiredArgsConstructor
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    private final JwtProvider jwtProvider;
//    private final UsersRepository usersRepository;
//    //jwt 필터
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        try{
//            String token = parseBearerToken(request);
//            if(token == null){
//                filterChain.doFilter(request, response);
//                return;
//            }
//            String userEmail = jwtProvider.validate(token); //토큰 넘기기
//            if(userEmail == null){
//                filterChain.doFilter(request, response);
//                return;
//            }
//            Optional<Users> users = usersRepository.findByEmail(userEmail);
////            Claims info = jwtProvider.getUserInfoFromToken(token);
//            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
//            AbstractAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userEmail, null)
//        }catch (Exception exception){
//            exception.printStackTrace();
//
//        }
//        filterChain.doFilter(request, response);
//    }
//
//    //request객체로 부터 토큰 값을 가져오는 역할
//    private String parseBearerToken(HttpServletRequest request){
//        //request로부터 Authorization받는다
//        String authorization = request.getHeader("Authorization");
//        boolean hasAuthorization = StringUtils.hasText(authorization); //Authorization 방식인지 확인
//        if(!hasAuthorization){
//            return null;
//        }
//        boolean isBearer = authorization.startsWith("Bearer "); //Bearer 방식인지 확인
//        if(!isBearer){
//            return null;
//        }
//
//        String token = authorization.substring(7); //"Bearer " 7번째 부터 토큰이므로 7번째부터 가져온다
//        return token;
//    }
//}
