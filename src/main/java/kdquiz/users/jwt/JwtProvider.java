//package kdquiz.jwt;
//
//import io.jsonwebtoken.*;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.nio.charset.StandardCharsets;
//import java.security.Key;
//import java.time.Instant;
//import java.time.temporal.ChronoUnit;
//import java.util.Date;
//import io.jsonwebtoken.security.Keys;
//이것도 사용 안하는거
//@Component
//@Slf4j
//public class JwtProvider {
//    @Value("${jwt.secret.key}")
//    private String secretKey;
//    Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
//    public String create(String userEmail) {
//        Date expiredDate = Date.from(Instant.now().plus(3, ChronoUnit.HOURS)); //토큰 만료시간 현재 시간으로부터 3시간
//        Key key = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
//
//        //jwt 생성
//        String jwt = Jwts.builder()
//                .signWith(key, SignatureAlgorithm.HS256) //서명과 관련된 부분, 암호화, 비밀키 관련
//                //payload(data) 부분
//                .setSubject(userEmail) //sub(email) 사용자 이메일이 들어감
//                .setIssuedAt(new Date()) //iat(현재시간) 현재 시간이 들어감
//                .setExpiration(expiredDate) //exp(만료시간) 토큰 만료 시간이 들어감
//                .compact();
//        return jwt;
//    }
//
//    //jwt 검증
//    public String validate(String jwt) {
//        String subject = null;
//        Claims claims = null; //jwt 받는 곳
//
//        try {
//            claims = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(jwt)
//                    .getBody();
//            subject = claims.getSubject();
//            return subject;
//        } catch (SecurityException | MalformedJwtException e){
//            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
//            e.printStackTrace();
//            return null;
//        } catch (Exception exception) {
//            log.info("Expired JWT token, 만료된 JWT token 입니다.");
//            exception.printStackTrace();
//            return null;
//        }
//    }
//
//    //토큰에서 사용자 정보 가져오기
//    public Claims getUserInfoFromToken(String token){
//        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token).getBody();
//    }
//
//}
