package com.musicmindproject.backend.logic.database;

import com.musicmindproject.backend.entities.Share;

import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class SharesManager extends DatabaseManager<Share> {
    @Override
    public Share store(Share item) {
        return entityManager.merge(item);
    }

    @Override
    public Share retrieve(Object id) {
        Share share = (Share)id;
        List<Share> shares = entityManager.createNamedQuery("Share.get", Share.class).setParameter("shared", share.getShared()).setParameter("share", share.getShare()).setParameter("type", share.getType()).getResultList();
        return shares.size() == 0 ? null : shares.get(0);
    }

    @Override
    public List<Share> retrieveMany(int min, int max, String query) {
        return null;
    }
}
