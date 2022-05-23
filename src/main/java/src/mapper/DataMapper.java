package src.mapper;

import org.springframework.stereotype.Component;

/**
 * It is responsible for converting the information between the entity stored in a DB vs the DTO exposed to the clients.
 *
 * @param <U> the type of the entity with the internal representation in a relational DB
 * @param <T> the type of the DTO derived from the fields of the entity, extracting only the necessary information and hiding
 *            sensitive information from the end users, such as the id or complex data structures.
 */
@Component
public interface DataMapper<U, T> {
    T mapToDto(U entity);

    U mapToEntity(T dto);
}
