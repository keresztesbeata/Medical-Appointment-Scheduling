package src.validator;

import org.springframework.beans.factory.annotation.Autowired;
import src.exceptions.InvalidDataException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

public class DataValidator<T> {

    @Autowired
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    public void validate(T entity) throws InvalidDataException {
        Validator validator = factory.getValidator();
        MessageBuilder messageBuilder = new MessageBuilder();

        Set<ConstraintViolation<T>> violations = validator.validate(entity);

        if (violations.isEmpty()) {
            return;
        }

        for (ConstraintViolation<T> violation : violations) {
            messageBuilder.append(violation.getMessage());
        }

        throw new InvalidDataException(messageBuilder.build());
    }
}
