package src.exceptions;

import org.springframework.stereotype.Component;

@Component
public class InvalidOperationException extends Exception{
    public InvalidOperationException() {
    }

    public InvalidOperationException(String message) {
        super(message);
    }
}
