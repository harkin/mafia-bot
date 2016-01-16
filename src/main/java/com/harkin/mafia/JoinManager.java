package com.harkin.mafia;

import com.google.common.collect.ImmutableList;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericMessageEvent;
import rx.Observable;
import rx.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class JoinManager {

    private static final int JOIN_PERIOD_S = 45;

    private static final int ARBITRARY_NUMBER = 5; //todo find a better home for this, probably wherever the roles per user count ends up living

    private final List<User> users = new ArrayList<>();
    private final Observable<GenericMessageEvent<PircBotX>> channelObs;
    private final OnJoinInteraction listener;
    private final Channel channel;

    private Subscription subscription;

    public JoinManager(Channel channel, Observable<GenericMessageEvent<PircBotX>> channelObs, OnJoinInteraction listener) {
        this.channel = channel;
        this.listener = listener;
        this.channelObs = channelObs;
    }

    public void begin(User user) {
        users.add(user);
        subscription = channelObs
                .take(JOIN_PERIOD_S, TimeUnit.SECONDS)
                .filter(event -> event.getMessage().startsWith("!"))
                .filter(event -> event.getMessage().toLowerCase().startsWith("!join"))
                .doOnNext(event -> event.respond(String.format("%s has joined the game!", event.getUser().getNick())))
                .subscribe(pircBotXMessageEvent -> users.add(pircBotXMessageEvent.getUser()),
                        throwable -> {},
                        this::endJoining);
    }

    private void endJoining() {
        subscription.unsubscribe();
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
