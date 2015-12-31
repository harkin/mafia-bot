package com.harkin.mafia;

import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import rx.Observable;

import java.util.List;

public class Mafia implements OnJoinInteraction, OnDayInteraction {
    private final UserManager userManager;
    private final JoinManager joinManager;
    private final DayManager dayManager;
    private final NightManager nightManager;

    private final Channel channel;

    public Mafia(Channel channel,
                 Observable<MessageEvent<PircBotX>> channelObs,
                 Observable<PrivateMessageEvent<PircBotX>> privateObs) {
        userManager = new UserManager();
        joinManager = new JoinManager(channel, channelObs, this);
        dayManager = new DayManager(channel, channelObs, this);
        nightManager = new NightManager(channel, channelObs, privateObs);

        this.channel = channel;
    }

    public void start(User user) {
        joinManager.begin(user);
    }

    @Override
    public void onPlayersJoined(List<User> players) {
        userManager.assignRoles(players, channel);
        nightManager.beginNight();
    }

    @Override
    public void onGameCancelled() {
        //todo alert gm that game is over
    }


}
