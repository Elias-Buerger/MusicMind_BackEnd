package com.musicmindproject.backend.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Available personality-categories with associated music genres
 */
public enum PsychologicalCategory {
    OPENNESS(120,200, Arrays.asList(MusicGenre.CLASSIC, MusicGenre.JAZZ)),
    CONSCIENTIOUSNESS(210,1000, Arrays.asList(MusicGenre.ROCK, MusicGenre.METAL)),
    EXTRAVERSION(60,130, Arrays.asList(MusicGenre.HIPHOP, MusicGenre.SOUL)),
    AGREEABLENESS(0,1000, null),
    NEUROTICISM(30,120, Collections.singletonList(MusicGenre.COUNTRY));

    private final List<MusicGenre> genres;
    private int minBpm;
    private int maxBpm;

    PsychologicalCategory(int minBpm, int maxBpm, List<MusicGenre> genres){
        this.minBpm = minBpm;
        this.maxBpm = maxBpm;
        this.genres = genres;
    }

    public List<MusicGenre> getGenres() {
        return genres;
    }

    public int getMinBpm() {
        return minBpm;
    }

    public void setMinBpm(int minBpm) {
        this.minBpm = minBpm;
    }

    public int getMaxBpm() {
        return maxBpm;
    }

    public void setMaxBpm(int maxBpm) {
        this.maxBpm = maxBpm;
    }
}
