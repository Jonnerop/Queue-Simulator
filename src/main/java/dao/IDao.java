package dao;

import java.util.List;

public interface IDao<Object> {
    void persist(Object object);
    Object find(int id);
    List<Object> findAll();
    void update(Object object);
    void delete(Object object);
}
