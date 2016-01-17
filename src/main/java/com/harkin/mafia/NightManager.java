package com.harkin.mafia;

import com.harkin.mafia.models.Role;
import javafx.util.Pair;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.types.GenericMessageEvent;
import rx.Observable;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class NightManager {

    private static final int NIGHT_LENGTH_S = 45;

    private final Observable<GenericMessageEvent<PircBotX>> channelObs;
    private final Observable<GenericMessageEvent<PircBotX>> privateObs;
    private final Channel channel;
    private final OnNightInteraction listener;

    private int nightCount = 1;

    public NightManager(Channel channel,
                        Observable<GenericMessageEvent<PircBotX>> channelObs,
                        Observable<GenericMessageEvent<PircBotX>> privateObs,
                        OnNightInteraction listener) {
        this.channel = channel;
        this.channelObs = channelObs;
        this.privateObs = privateObs;
        this.listener = listener;
    }

    public void beginNight(Map<String, Role> players) {
        channel.send().message(String.format("Night %d begins. The players still alive are: %s. The night will end in %d seconds", nightCount, players.keySet(), NIGHT_LENGTH_S));

        List<Pair<Role, Role>> actions = new ArrayList<>(players.size());

        channelObs
                .mergeWith(privateObs)
                .take(NIGHT_LENGTH_S, TimeUnit.SECONDS)
                .filter(pircBotXMessageEvent -> isValidNightAction(pircBotXMessageEvent, players))
                .map(pircBotXMessageEvent -> new Pair<>(players.get(pircBotXMessageEvent.getUser().getNick()),
                        players.get(pircBotXMessageEvent.getMessage().split(" ")[1])))
                .subscribe(
                        pair -> actions.set(pair.getKey().getPriority(), pair),
                        throwable -> { },  //todo handle errors
                        () -> endNight(evaluateActions(actions)));

        //todo end early once all actions are in

        //todo prompt players to use night actions
    }

    //todo ewww I don't like this at all :(
    private List<Role> evaluateActions(List<Pair<Role, Role>> actions) {
        List<Role> blockedUsers = new ArrayList<>();
        List<Role> protectedUsers = new ArrayList<>();
        List<Role> murderedUsers = new ArrayList<>();

        actions.stream().filter(pair -> pair != null).forEach(pair -> {
            int priority = pair.getKey().getPriority();
            if (!blockedUsers.contains(pair.getKey())) {
                if (priority == Role.PRIORITY_HOOKER) {
                    blockedUsers.add(pair.getValue());
                } else if (priority == Role.PRIORITY_BODYGUARD) {
                    protectedUsers.add(pair.getValue());
                } else if (priority == Role.PRIORITY_WEREWOLF) {
                    if (!protectedUsers.contains(pair.getValue())) {
                        murderedUsers.add(pair.getValue());
                    }
                } else if (priority == Role.PRIORITY_MAFIA) {
                    if (!protectedUsers.contains(pair.getValue())) {
                        murderedUsers.add(pair.getValue());
                    }
                } else if (priority == Role.PRIORITY_INSPECTOR) {
                    pair.getKey().getUser().send().message(pair.getValue().toString());
                } else if (priority == Role.PRIORITY_SILENCER) {
                    //todo
                }
            }
        });

        return murderedUsers;
    }

    private void endNight(List<Role> theMurdered) {
        if (theMurdered.isEmpty()) {
            channel.send().message("No one was killed.");
        } else {
            for (Role deceased : theMurdered) {
                channel.send().message(String.format("%s (%s) was killed!", deceased.getUser().getNick(), deceased));
            }
        }

        listener.onNightEnd(theMurdered);
        nightCount++;
    }

    private boolean isValidNightAction(GenericMessageEvent<PircBotX> pircBotXMessageEvent, Map<String, Role> players) {
        String[] parts = pircBotXMessageEvent.getMessage().split(" ");
        return parts.length >= 2
                && players.containsKey(pircBotXMessageEvent.getUser().getNick())
                && parts[0].toLowerCase().equals(players.get(pircBotXMessageEvent.getUser().getNick()).getNightActionText())
                && players.containsKey(parts[1]);
    }
}
