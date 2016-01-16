package com.harkin.mafia;

import com.harkin.mafia.models.Role;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericMessageEvent;
import rx.Observable;

import java.util.List;

public class Mafia implements OnJoinInteraction, OnDayInteraction, OnNightInteraction {
    private final UserManager userManager;
    private final JoinManager joinManager;
    private final DayManager dayManager;
    private final NightManager nightManager;

    private final Channel channel;

    public Mafia(Channel channel,
                 Observable<GenericMessageEvent<PircBotX>> channelObs,
                 Observable<GenericMessageEvent<PircBotX>> privateObs) {
        userManager = new UserManager();
        joinManager = new JoinManager(channel, channelObs, this);
        dayManager = new DayManager(channel, channelObs, this);
        nightManager = new NightManager(channel, channelObs, privateObs, this);

        this.channel = channel;
    }

    public void start(User user) {
        joinManager.begin(user);
    }

    @Override
    public void onPlayersJoined(List<User> players) {
        userManager.assignRoles(players, channel);
        nightManager.beginNight(userManager.getPlayers());
    }

    @Override
    public void onGameCancelled() {
        //todo alert godfather that game is over
    }

    @Override
    public void onDayEnd(Role role) {
        if (role != null) {
            userManager.killPlayer(role.getUser().getNick());
        }
        nightManager.beginNight(userManager.getPlayers());
    }

    @Override
    public void onNightEnd(List<Role> theMurdered) {
        for (Role deceased : theMurdered) {
            userManager.killPlayer(deceased.getUser().getNick());
        }
        dayManager.beginDay(userManager.getPlayers());
    }


}
