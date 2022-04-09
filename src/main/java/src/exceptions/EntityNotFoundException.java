package src.exceptions;

import org.springframework.stereotype.Component;

@Component
public class EntityNotFoundException extends Exception{
    public EntityNotFoundException() {
    }

    public EntityNotFoundException(String message) {
        super(message);
    }
}
