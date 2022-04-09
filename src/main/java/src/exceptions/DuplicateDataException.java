package src.exceptions;

import org.springframework.stereotype.Component;

@Component
public class DuplicateDataException extends Exception{
    public DuplicateDataException() {
    }

    public DuplicateDataException(String message) {
        super(message);
    }
}
