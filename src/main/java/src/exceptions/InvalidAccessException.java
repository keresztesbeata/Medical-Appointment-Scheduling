package src.exceptions;

import org.springframework.stereotype.Component;

@Component
public class InvalidAccessException extends Exception{
    public InvalidAccessException() {
    }

    public InvalidAccessException(String message) {
        super(message);
    }
}
