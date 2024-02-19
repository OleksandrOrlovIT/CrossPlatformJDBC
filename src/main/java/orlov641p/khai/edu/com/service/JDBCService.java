package orlov641p.khai.edu.com.service;

import java.util.List;

public interface JDBCService<T> {
    List<T> findAll();
    T getById(String id);

    boolean deleteById(String id);

    boolean add(T object);

    boolean update(T object);

    boolean deleteAll();
}
