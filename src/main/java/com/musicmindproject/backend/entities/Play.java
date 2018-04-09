package com.musicmindproject.backend.entities;

import javax.persistence.*;

@Entity
@Table(name = "PLAYS")
@NamedQuery(name = "Play.get", query = "SELECT p FROM Play p WHERE p.played = :played AND p.player = :player")
public class Play {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PLAY_ID")
    private Integer playId;
    @Column(name = "PLAYER_USERID")
    private String player;
    @Column(name = "PLAYED_USERID")
    private String played;

    public Play(Integer playId, String player, String played) {
        this.playId = playId;
        this.player = player;
        this.played = played;
    }

    public Play() {
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }

    public String getPlayed() {
        return played;
    }

    public void setPlayed(String played) {
        this.played = played;
    }

    public long getPlayId() {
        return playId;
    }

    public void setPlayId(Integer playId) {
        this.playId = playId;
    }
}
