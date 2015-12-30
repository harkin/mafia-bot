package com.harkin.mafia;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JoinManager extends ListenerAdapter<PircBotX> {

    private static final int JOIN_PERIOD_S = 45;

    private static final int ARBITRARY_NUMBER = 5; //todo find a better home for this, probably wherever the roles per user count ends up living

    private final List<User> users = new ArrayList<>();
    private final OnJoinInteraction listener;
    private final Channel channel;

    public JoinManager(Channel channel, OnJoinInteraction listener) {
        this.channel = channel;
        this.listener = listener;
    }

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        if (event.getMessage().toLowerCase().equals("!join")) {
            event.respond(String.format("%s has joined the game! Current players: %s",
                    event.getUser().getNick(),
                    Joiner.on(",").join(users)));
        }
        //todo some sort of game cancel here maybe
    }

    public void begin(User user) {
        users.add(user);
        Executors.newSingleThreadScheduledExecutor().schedule(this::endJoining, JOIN_PERIOD_S, TimeUnit.SECONDS);
    }

    private void endJoining() {
        if (users.size() > ARBITRARY_NUMBER) {
            channel.send().message("Not enough people joined to start the game :(");
            listener.onGameCancelled();
        } else {
            channel.send().message("The game is starting! You'll be messaged your role in PM");
            listener.onPlayersJoined(ImmutableList.copyOf(users));
        }
        users.clear();
    }
}
