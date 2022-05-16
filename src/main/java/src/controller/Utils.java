package src.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import src.config.UserDetailsImpl;
import src.exceptions.EntityNotFoundException;
import src.model.users.Account;

public class Utils {
    private static final String NO_LOGGED_IN_USER_ERROR_MESSAGE = "No logged in user!";

    public static Account getCurrentUserAccount() throws EntityNotFoundException{
        Object currentUserAccount = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(currentUserAccount instanceof UserDetailsImpl) {
            return ((UserDetailsImpl) currentUserAccount).getAccount();
        }else{
            throw new EntityNotFoundException(NO_LOGGED_IN_USER_ERROR_MESSAGE);
        }
    }
}
