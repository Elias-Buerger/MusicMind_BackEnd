package com.musicmindproject.backend.entities.enums;

/**
 * Available instruments
 */
public enum Instrument {
    GUITAR(InstrumentType.STRINGED),
    TRUMPET(InstrumentType.BLOW),
    TUBA(InstrumentType.BLOW),
    BANJO(InstrumentType.STRINGED),
    HORN(InstrumentType.BLOW),
    CLARINET(InstrumentType.BLOW),
    TROMBONE(InstrumentType.BLOW),
    CONTRABASS(InstrumentType.STRINGED),
    DRUMS(InstrumentType.DRUM),
    CONGA(InstrumentType.DRUM),
    PIANO(InstrumentType.KEY),
    OBOE(InstrumentType.BLOW),
    BASSOON(InstrumentType.BLOW),
    PANPIPES(InstrumentType.BLOW),
    FLUTE(InstrumentType.BLOW),
    ALPHORN(InstrumentType.BLOW),
    CELLO(InstrumentType.STRINGED),
    VIOLIN(InstrumentType.STRINGED),
    ORGAN(InstrumentType.KEY),
    HARP(InstrumentType.STRINGED),
    TAMBURIN(InstrumentType.DRUM),
    TRIANGLE(InstrumentType.DRUM),
    CASTANET(InstrumentType.DRUM),
    KETTLEDRUM(InstrumentType.DRUM),
    BASSGUITAR(InstrumentType.STRINGED),
    HAMMONDORGAN(InstrumentType.KEY),
    EGUITAR(InstrumentType.STRINGED),
    CLAVINET(InstrumentType.KEY),
    HARMONICA(InstrumentType.BLOW),
    MANDOLINE(InstrumentType.STRINGED),
    ACCORDION(InstrumentType.HANDTENSION),
    SAXOPHONE(InstrumentType.BLOW);

    private InstrumentType instrumentType;

    Instrument(InstrumentType instrumentType) {
        this.instrumentType = instrumentType;
    }

    public InstrumentType getInstrumentType() {
        return instrumentType;
    }
}
