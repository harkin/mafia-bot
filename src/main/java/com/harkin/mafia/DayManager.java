package com.harkin.mafia;

import com.harkin.mafia.models.Role;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.events.MessageEvent;
import rx.Observable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DayManager {

    private static final int DAY_LENGTH_S = 30;

    private final Map<Role, Integer> votes = new HashMap<>();

    private final Observable<MessageEvent<PircBotX>> channelObs;
    private final Channel channel;
    private final OnDayInteraction listener;

    private int dayCount = 1;

    public DayManager(Channel channel, Observable<MessageEvent<PircBotX>> channelObs, OnDayInteraction listener) {
        this.channel = channel;
        this.channelObs = channelObs;
        this.listener = listener;
    }

    public void beginDay(List<Role> players) {
        channel.send().message(String.format("Day %d begins. Who will you lynch? Type !vote {user} to vote", dayCount));

        channelObs
                .take(DAY_LENGTH_S, TimeUnit.SECONDS)
                .filter(event -> event.getMessage().startsWith("!vote"))
                .map(event -> event.getMessage().split(" "))
                .filter(strings -> strings.length < 2)
                .map(strings -> strings[1])
                .flatMap(playerName -> Observable.from(players)
                        .filter(role -> role.getUser().getNick().equals(playerName)))
                .subscribe(this::castVote,
                        throwable -> {/*todo*/},
                        this::endDay);
    }

    private void castVote(Role role) {
        if (votes.containsKey(role)) {
            votes.put(role, votes.get(role) + 1);
        } else {
            votes.put(role, 1);
        }
    }

    //todo test how this compares to the rx concoction above for seeing if a vote is valid
    private Role example(String s, List<Role> players) {
        if (s.startsWith("!vote")) {
            String[] words = s.split(" ");
            if (words.length >= 2) {
                String name = words[1];
                for (Role r : players) {
                    if (r.getUser().getNick().equals(s)) {
                        return r;
                    }
                }
            }
        }
        return null;
    }

    private void endDay() {
        Role role = findWhoGotLynched();
        dayCount++;
    }

    //todo this could surely be tidied up
    private Role findWhoGotLynched() {
        Role lynched = null;
        int localMax = 0;
        for (Map.Entry<Role, Integer> entry : votes.entrySet()) {
            if (entry.getValue() > localMax) {
                lynched = entry.getKey();
                localMax = entry.getValue();
            } else if (entry.getValue() == localMax) {
                //there's a tie, lynch no one
                lynched = null;
            }
        }

        return lynched;
    }
}
