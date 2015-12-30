package com.harkin.mafia;

import org.pircbotx.Channel;
import org.pircbotx.User;

import java.util.List;

public class Mafia implements OnJoinInteraction {
    private final UserManager userManager;
    private final JoinManager joinManager;
    private final DayManager dayManager;
    private final NightManager nightManager;

    private final Channel channel;

    public Mafia(Channel channel) {
        userManager = new UserManager();
        joinManager = new JoinManager(channel, this);
        dayManager = new DayManager(channel);
        nightManager = new NightManager(channel);

        this.channel = channel;
    }

    public void start(User user){
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
