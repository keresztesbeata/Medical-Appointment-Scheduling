package src.mapper;

import org.springframework.stereotype.Component;

@Component
public interface DataMapper<U, T> {
    T mapToDto(U entity);

    U mapToEntity(T dto);
}
