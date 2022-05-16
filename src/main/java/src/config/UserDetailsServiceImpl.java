package src.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import src.model.users.Account;
import src.repository.AccountRepository;

import javax.transaction.Transactional;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String USERNAME_NOT_FOUND_ERROR_MESSAGE = "Username is not found!";
    private static final String ID_NOT_FOUND_ERROR_MESSAGE = "Id is not found!";

    @Autowired
    AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(USERNAME_NOT_FOUND_ERROR_MESSAGE));
        return new UserDetailsImpl(account);
    }

    // This method is used by JWTAuthenticationFilter
    @Transactional
    public UserDetails loadUserById(Integer id) {
        Account account = accountRepository.findById(id).orElseThrow(
                () -> new UsernameNotFoundException(ID_NOT_FOUND_ERROR_MESSAGE));
        return new UserDetailsImpl(account);
    }
}
