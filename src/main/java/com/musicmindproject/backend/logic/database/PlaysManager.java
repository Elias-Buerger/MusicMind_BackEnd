package com.musicmindproject.backend.logic.database;

import com.musicmindproject.backend.entities.Play;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class PlaysManager extends DatabaseManager<Play> {
    @Override
    public Play store(Play item) {
        return entityManager.merge(item);
    }

    @Override
    public List<Play> retrieveAll() {
        return null;
    }

    @Override
    public Play retrieve(Object id) {
        Play play = (Play)id;
        List<Play> plays = entityManager.createNamedQuery("Play.get", Play.class).setParameter("played", play.getPlayed()).setParameter("player", play.getPlayer()).getResultList();
        return plays.size() == 0 ? null : plays.get(0);
    }

    @Override
    public List<Play> retrieveMany(int min, int max, String query) {
        return null;
    }
}
