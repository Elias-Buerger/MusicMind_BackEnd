package com.musicmindproject.backend.logic.database;

import com.musicmindproject.backend.entities.Play;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class PlaysManager extends DatabaseManager<Play> {
    @Override
    public void store(Play item) {
        entityManager.merge(item);
    }

    @Override
    public List<Play> retrieveAll() {
        return null;
    }

    @Override
    public Play retrieve(Object id) {
        return entityManager.createNamedQuery("Play.get", Play.class).getSingleResult();
    }

    @Override
    public List<Play> retrieveMany(int min, int max, String query) {
        return null;
    }
}
