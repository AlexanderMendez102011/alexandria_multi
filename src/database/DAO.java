package database;

interface DAO<T> {

    T load(Object obj);

    boolean update(T obj);
}