package com.musicmindproject.backend.logic;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless
public abstract class DatabaseManager<T> {
    static final String CONNECTION_STRING = "jdbc:mysql://musicmindproject.com/MusicMindDB";
    static final String USER = "root";
    static final String PASSWORD = "G6GzwxHT";
    @PersistenceContext
    EntityManager entityManager;

    DatabaseManager(){}

    public abstract void store(T item);
    public abstract List<T> retrieveAll();
    public abstract T retrieve(Object id);
    public abstract List<T> retrieveMany(int min, int max);
}
