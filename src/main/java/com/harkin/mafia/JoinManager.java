package com.harkin.mafia;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import rx.Observable;
import rx.Subscription;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class JoinManager {

    private static final int JOIN_PERIOD_S = 45;

    private static final int ARBITRARY_NUMBER = 5; //todo find a better home for this, probably wherever the roles per user count ends up living

    private final List<User> users = new ArrayList<>();
    private final OnJoinInteraction listener;
    private final Channel channel;
    private final Observable<MessageEvent<PircBotX>> channelObs;

    private Subscription subscription;

    public JoinManager(Channel channel, Observable<MessageEvent<PircBotX>> channelObs, OnJoinInteraction listener) {
        this.channel = channel;
        this.listener = listener;
        this.channelObs = channelObs;
    }

    public void begin(User user) {
        users.add(user);
        Executors.newSingleThreadScheduledExecutor().schedule(this::endJoining, JOIN_PERIOD_S, TimeUnit.SECONDS);
        //todo is there a way to unsubscribe after a defined period of time? Would make the executor unnecessary
        subscription = channelObs
                .filter(event -> event.getMessage().startsWith("!"))
                .filter(event -> event.getMessage().toLowerCase().startsWith("!join"))
                .doOnNext(event -> event.respond(String.format("%s has joined the game! Current players: %s",
                        event.getUser().getNick(),
                        Joiner.on(",").join(users))))
                .subscribe(pircBotXMessageEvent -> users.add(pircBotXMessageEvent.getUser()));
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
