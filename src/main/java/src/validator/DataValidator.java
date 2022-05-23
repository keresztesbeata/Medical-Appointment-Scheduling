package src.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import src.exceptions.InvalidDataException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * Validates the attributes of a given entity. It is implemented in a generic way to support any type of class.
 * The validation is based on the constraints added in form of Spring annotations on the respective entities.
 * It is based on the Factory design pattern, implemented in spring.
 *
 * @param <T> the generic type of the entity to be validated
 */
@Component
public class DataValidator<T> {

    @Autowired
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();

    /**
     * Checks if the given entity contains only valid data.
     *
     * @param entity the entity to be verified
     * @throws InvalidDataException of some data is incorrect
     */
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
