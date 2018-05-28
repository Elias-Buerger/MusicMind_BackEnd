package com.musicmindproject.backend.entities;

import javax.persistence.*;

@Entity
@Table(name = "SHARES")
@NamedQuery(name = "Share.get", query = "SELECT s FROM Share s WHERE s.share = :share AND s.shared = :shared AND s.type = :type")
public class Share {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SHARE_ID")
    private Integer playId;
    @Column(name = "SHARE_USERID")
    private String share;
    @Column(name = "SHARED_USERID")
    private String shared;
    @Column(name = "TYPE")
    private String type;

    public Share(Integer playId, String share, String shared, String type) {
        this.playId = playId;
        this.share = share;
        this.shared = shared;
        this.type = type;
    }

    public Share() {
    }

    public Integer getPlayId() {
        return playId;
    }

    public void setPlayId(Integer playId) {
        this.playId = playId;
    }

    public String getShare() {
        return share;
    }

    public void setShare(String share) {
        this.share = share;
    }

    public String getShared() {
        return shared;
    }

    public void setShared(String shared) {
        this.shared = shared;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
