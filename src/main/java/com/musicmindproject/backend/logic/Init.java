package com.musicmindproject.backend.logic;

import com.musicmindproject.musicgenerator.magenta.MidiGeneratorRunner;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;

@Singleton
@Startup
public class Init {

    @PostConstruct
    public void init() {
        //MidiGeneratorRunner.getInstance().init();
    }
}
