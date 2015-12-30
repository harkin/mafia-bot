package com.harkin.mafia;

import org.pircbotx.Channel;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class NightManager {

    private static final int NIGHT_LENGTH_S = 30;

    private final OnNightInteraction listener;
    private final Channel channel;
    private int nightCount = 0;

    public NightManager(Channel channel, OnNightInteraction listener) {
        this.channel = channel;
        this.listener = listener;
    }


    public void beginNight() {
        Executors.newSingleThreadScheduledExecutor().schedule(this::endNight, NIGHT_LENGTH_S, TimeUnit.SECONDS);
    }

    private void endNight(){

    }
}
