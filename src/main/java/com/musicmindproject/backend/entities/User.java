package com.musicmindproject.backend.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER")
@NamedQueries({
    @NamedQuery(name = "User.getAll", query = "SELECT u FROM User u"),
    @NamedQuery(name = "User.hottest", query = "SELECT u FROM User u ORDER BY u.plays"),
    @NamedQuery(name = "User.newest", query = "SELECT u FROM User u ORDER BY u.dateOfCreation DESC"),
    @NamedQuery(name = "User.getByName", query = "SELECT u FROM User u WHERE u.userName LIKE :uName")
})

public class User implements Serializable {
    @Id
    private String userId;
    private String userName;

    @Column(name = "PATH")
    private String pathToMusicTrack;
    private double openness;
    private double conscientiousness;
    private double extraversion;
    private double agreeableness;
    private double neuroticism;
    private int shares;
    private int plays;

    @Column(name = "DATE_OF_CREATION")
    private Timestamp dateOfCreation;

    public User() {
    }

    public User(String userId, String userName, String pathToMusicTrack, double openness, double conscientiousness, double extraversion, double agreeableness, double neuroticism) {
        this.userId = userId;
        this.userName = userName;
        this.pathToMusicTrack = pathToMusicTrack;
        this.openness = openness;
        this.conscientiousness = conscientiousness;
        this.extraversion = extraversion;
        this.agreeableness = agreeableness;
        this.neuroticism = neuroticism;
        this.shares = 0;
        this.plays = 0;
        this.dateOfCreation = Timestamp.valueOf(LocalDateTime.now());
    }

    public User(String userId, String userName, String pathToMusicTrack, double openness, double conscientiousness, double extraversion, double agreeableness, double neuroticism, int shares, int plays, Timestamp dateOfCreation) {
        this.userId = userId;
        this.userName = userName;
        this.pathToMusicTrack = pathToMusicTrack;
        this.openness = openness;
        this.conscientiousness = conscientiousness;
        this.extraversion = extraversion;
        this.agreeableness = agreeableness;
        this.neuroticism = neuroticism;
        this.shares = shares;
        this.plays = plays;
        this.dateOfCreation = dateOfCreation;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPathToMusicTrack() {
        return pathToMusicTrack;
    }

    public void setPathToMusicTrack(String pathToMusicTrack) {
        this.pathToMusicTrack = pathToMusicTrack;
    }

    public double getOpenness() {
        return openness;
    }

    public void setOpenness(double openness) {
        this.openness = openness;
    }

    public double getConscientiousness() {
        return conscientiousness;
    }

    public void setConscientiousness(double conscientiousness) {
        this.conscientiousness = conscientiousness;
    }

    public double getExtraversion() {
        return extraversion;
    }

    public void setExtraversion(double extraversion) {
        this.extraversion = extraversion;
    }

    public double getAgreeableness() {
        return agreeableness;
    }

    public void setAgreeableness(double agreeableness) {
        this.agreeableness = agreeableness;
    }

    public double getNeuroticism() {
        return neuroticism;
    }

    public void setNeuroticism(double neuroticism) {
        this.neuroticism = neuroticism;
    }

    public int getShares() {
        return shares;
    }

    public void setShares(int shares) {
        this.shares = shares;
    }

    public int getPlays() {
        return plays;
    }

    public void setPlays(int plays) {
        this.plays = plays;
    }

    public Timestamp getDateOfCreation() {
        return dateOfCreation;
    }

    public void setDateOfCreation(Timestamp dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }
}
