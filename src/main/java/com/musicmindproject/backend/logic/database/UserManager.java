package com.musicmindproject.backend.logic.database;

import com.musicmindproject.backend.entities.User;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class UserManager extends DatabaseManager<User> {
    @Override
    public void store(User item) {
        entityManager.merge(item);
    }

    @Override
    public List<User> retrieveAll() {
        return entityManager.createNamedQuery("User.getAll", User.class).getResultList();
    }

    @Override
    public User retrieve(Object id) {
        return entityManager.find(User.class, id);
    }

    /**
     * @param query
     * Fixed Keywords:
     * - newest
     * - hottest
     * - everything else: name of the user / music-track
     * @param min
     * Ignore
     * @param max
     * Ignore
     * @return List of Users
     */
    @Override
    public List<User> retrieveMany(int min, int max, String query) {
        if(query.equals("hottest") || query.equals("newest"))
            return entityManager.createNamedQuery("User." + query, User.class).setFirstResult(min).setMaxResults(max - min).getResultList();
        return entityManager.createNamedQuery("User.getByName", User.class).setParameter("uName", query).getResultList();
    }
}
