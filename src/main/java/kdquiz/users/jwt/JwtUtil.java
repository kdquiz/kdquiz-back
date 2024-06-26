package kdquiz.users.jwt;

import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import kdquiz.usersecurity.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;
import org.springframework.util.StringUtils;

import java.security.Key;

import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long TOKEN_TIME = 2 * 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_TIME = 30 * 24 * 60 * 60 * 1000L; // 30 days
    private final UserDetailsServiceImpl userDetailsService;

    @Value("${jwt.secret.key}")
    private String secretKey;

    @Value("${jwt.refresh.secret.key}")
    private String refreshSecretKey;

    private Key key;
    private Key refreshKey;

    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init(){
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);

        byte[] refreshBytes = Base64.getDecoder().decode(refreshSecretKey);
        refreshKey = Keys.hmacShaKeyFor(refreshBytes);
    }

    //header 토큰 가져오기
    public String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)){
            System.out.println("토큰 값: "+bearerToken.substring(7));
            return bearerToken.substring(7);
        }
        return null;
    }

    //토큰 생성
    public String createToken(String userEmail){
        Date date = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userEmail)
                        .claim(AUTHORIZATION_KEY, "user")
                        .setExpiration(new Date(date.getTime()+TOKEN_TIME))
                        .setIssuedAt(date)
                        .setHeaderParam("typ", "jwt")
                        .signWith(key, signatureAlgorithm)

                        .compact();
    }

    // 리프레시 토큰 생성
    public String createRefreshToken(String userEmail) {
        Date now = new Date();
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(userEmail)
                        .claim(AUTHORIZATION_KEY, "refresh")
                        .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_TIME))
                        .setIssuedAt(now)
                        .setHeaderParam("typ", "jwt")
                        .signWith(refreshKey, signatureAlgorithm)
                        .compact();
    }

    //토큰 검증
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e){
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e){
            log.info("Expried JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e){
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e){
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    //토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token){
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    //인증 객체 생성
    public Authentication createAuthentication(String username){
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
