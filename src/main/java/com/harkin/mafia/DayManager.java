package com.harkin.mafia;

import com.harkin.mafia.models.Role;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;
import rx.Observable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class DayManager {

    private static final int DAY_LENGTH_S = 30;

    private final Map<Role, Integer> votes = new HashMap<>();

    private final Observable<GenericMessageEvent<PircBotX>> channelObs;
    private final Channel channel;
    private final OnDayInteraction listener;

    private int dayCount = 1;

    public DayManager(Channel channel, Observable<GenericMessageEvent<PircBotX>> channelObs, OnDayInteraction listener) {
        this.channel = channel;
        this.channelObs = channelObs;
        this.listener = listener;
    }

    public void beginDay(Map<String, Role> players) {
        channel.send().message(String.format("Day %d begins. Who will you lynch? Type !vote {user} to vote", dayCount));

        //todo MAKE IT SO ONLY PEOPLE IN THE GAME CAN VOTE AHHHH
        channelObs
                .take(DAY_LENGTH_S, TimeUnit.SECONDS)
                .filter(pircBotXGenericMessageEvent -> isValidDayAction(pircBotXGenericMessageEvent, players))
                .map(event -> event.getMessage().split(" ")[1])
                .map(players::get)
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

    private void endDay() {
        Role role = findWhoGotLynched();
        if (role == null) {
            channel.send().message("The day ends with no lynching");
        } else {
            channel.send().message(String.format("The angry mob has lynched %s", role.getUser().getNick()));
        }
        listener.onDayEnd(role);
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

    private boolean isValidDayAction(GenericMessageEvent<PircBotX> pircBotXMessageEvent, Map<String, Role> players) {
        String[] parts = pircBotXMessageEvent.getMessage().split(" ");
        return parts.length >= 2
                && players.containsKey(pircBotXMessageEvent.getUser().getNick())
                && parts[0].toLowerCase().equals("!vote")
                && players.containsKey(parts[1]);
    }
}
