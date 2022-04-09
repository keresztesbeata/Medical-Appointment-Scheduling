package src.exceptions;

import org.springframework.stereotype.Component;

@Component
public class InvalidDataException extends Exception {
    public InvalidDataException() {
    }

    public InvalidDataException(String message) {
        super(message);
    }
}
