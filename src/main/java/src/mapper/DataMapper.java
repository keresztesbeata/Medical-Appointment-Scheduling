package src.mapper;

public interface DataMapper<T,U> {
    U mapToDto(T entity);
    T mapToEntity(U dto);
}
