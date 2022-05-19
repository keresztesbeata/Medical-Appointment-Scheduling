package src.exceptions;

import org.springframework.stereotype.Component;

@Component
public class InvalidStateException extends Exception{
    public InvalidStateException() {
    }

    public InvalidStateException(String message) {
        super(message);
    }
}
