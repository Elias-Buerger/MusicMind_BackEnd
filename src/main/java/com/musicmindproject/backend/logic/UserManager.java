package com.musicmindproject.backend.logic;

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

    @Override
    public List<User> retrieveMany(int min, int max) {
        return null;
    }
}
