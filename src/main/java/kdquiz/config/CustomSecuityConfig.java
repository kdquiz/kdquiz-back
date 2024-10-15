package kdquiz.config;

import kdquiz.users.jwt.JwtAuthFilter;
import kdquiz.users.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableWebSecurity

public class CustomSecuityConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.csrf().disable().authorizeRequests()
                // 밑에 5명 url 토큰 인증없이 바로 실행 가능
                .requestMatchers("/api/v1/participants").permitAll()
                .requestMatchers("/api/v1/game/participants").permitAll()
                .requestMatchers("/api/v1/game/participants/{pin}").permitAll()
                .requestMatchers("/api/v1/game/participants/{pin}/{nickname}").permitAll()
                .requestMatchers("/api/v1/users/register").permitAll()
                .requestMatchers("/api/v1/users/login").permitAll()
                .requestMatchers("/api/v1/mailSend").permitAll()
                .requestMatchers("/api/v1/mailAuthCheck").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll() //스웨거
                .requestMatchers("/v3/api-docs/**").permitAll() //스웨거
                .requestMatchers("/swagger-ui.html/**").permitAll()//스웨거
                .requestMatchers("/api/v1/users/get").permitAll()
                .requestMatchers("/api/v1/game/participants").permitAll()
                .requestMatchers("/api/v1/answer/**").permitAll()
                .requestMatchers("/api/v1/ranking/**").permitAll()
                .requestMatchers("/api/v1/gameJoin/**").permitAll()
                .requestMatchers("/error").permitAll()
                // 나머지들은 토큰 있어야 가능
                .anyRequest().authenticated()
                //로그인 커스텀 나중에 개발 할때 주석 처리 풀어
//                .and()
//                    .formLogin()//로그인 커스텀
//                    .loginPage("/api/v1/users/login")//로그인 페이지
//                    .loginProcessingUrl("/api/v1/users/login") //음 (form action) 값인거 같음 로그인 폼 제출 시 POST 요청을 보낼 URL
//                    .usernameParameter("/user") //이거는 form name인거 같음
//                    .defaultSuccessUrl("/api/v1/quiz/user/") //이거는 로그인 성공 하면 이 페이지로 이동 리다리렉션
//                    .permitAll()
//                .and()
//                    .logout()//이건 로그아웃
//                    .logoutRequestMatcher(new AntPathRequestMatcher("api/v1/users/logout")) //로그아웃 api
//                    .logoutSuccessUrl("/")//로그아웃 성공하면 메인페이지로 리다리렉션
//                    .permitAll()
                .and()
                    .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    //리액트랑 데이터를 주고받기 위한 설정
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                //.allowCredentials(true) //주석처리함
                .allowedOriginPatterns("*") //추가한 부분
                .exposedHeaders("*");
    }
    //사용자 비밀번호 암호화
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
