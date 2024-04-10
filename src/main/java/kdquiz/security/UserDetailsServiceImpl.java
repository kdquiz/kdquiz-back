package kdquiz.security;

import kdquiz.users.domain.Users;
import kdquiz.users.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException{
        Users users = usersRepository.findByEmail(userEmail).orElseThrow(()->
                new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
        return new UserDetailsImpl(users, userEmail);
    }
}
