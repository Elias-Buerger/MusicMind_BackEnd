package com.musicmindproject.backend.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Available genres
 */
public enum MusicGenre {
    JAZZ(Arrays.asList(
            Instrument.SAXOPHONE,
            Instrument.TRUMPET,
            Instrument.TUBA,
            Instrument.BANJO,
            Instrument.HORN,
            Instrument.TROMBONE,
            Instrument.CLARINET,
            Instrument.GUITAR,
            Instrument.CONTRABASS,
            Instrument.DRUMS,
            Instrument.CONGA,
            Instrument.PIANO)),

    CLASSIC(Arrays.asList(
            Instrument.OBOE,
            Instrument.BASSOON,
            Instrument.PANPIPES,
            Instrument.FLUTE,
            Instrument.ALPHORN,
            Instrument.SAXOPHONE,
            Instrument.TRUMPET,
            Instrument.CONTRABASS,
            Instrument.CELLO,
            Instrument.VIOLIN,
            Instrument.PIANO,
            Instrument.ORGAN,
            Instrument.HARP,
            Instrument.TAMBURIN,
            Instrument.TRIANGLE,
            Instrument.CASTANET,
            Instrument.KETTLEDRUM
    )),

    ROCK(Arrays.asList(
            Instrument.GUITAR,
            Instrument.BASSGUITAR,
            Instrument.DRUMS,
            Instrument.PIANO,
            Instrument.HAMMONDORGAN
    )),

    SOUL(Arrays.asList(
            Instrument.DRUMS,
            Instrument.PIANO,
            Instrument.HAMMONDORGAN,
            Instrument.CLAVINET,
            Instrument.HORN,
            Instrument.EGUITAR,
            Instrument.BASSGUITAR,
            Instrument.GUITAR
    )),

    HIPHOP(Collections.singletonList(
            Instrument.DRUMS
    )),
    BLUES(Collections.singletonList(
            Instrument.DRUMS
    )),
    COUNTRY(Arrays.asList(
            Instrument.DRUMS,
            Instrument.PIANO,
            Instrument.HARMONICA,
            Instrument.GUITAR,
            Instrument.BANJO,
            Instrument.MANDOLINE,
            Instrument.CONTRABASS,
            Instrument.VIOLIN,
            Instrument.ACCORDION
    )),
    METAL(Stream.concat(Stream.of(
            Instrument.GUITAR,
            Instrument.EGUITAR,
            Instrument.BASSGUITAR,
            Instrument.DRUMS
    ), CLASSIC.instruments.stream()).collect(Collectors.toList()));
//--------
    private List<Instrument> instruments;

    MusicGenre(List<Instrument> instruments) {
        this.instruments = instruments;
    }

    public List<Instrument> getInstruments() {
        return instruments;
    }
}