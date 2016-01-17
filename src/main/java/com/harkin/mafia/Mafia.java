package com.harkin.mafia;

import com.harkin.mafia.models.Role;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.hooks.types.GenericMessageEvent;
import rx.Observable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mafia implements OnJoinInteraction, OnDayInteraction, OnNightInteraction {
    private final UserManager userManager;
    private final JoinManager joinManager;
    private final DayManager dayManager;
    private final NightManager nightManager;
    private final GameOverListener gameOverListener;
    private final Channel channel;

    public Mafia(Channel channel,
                 Observable<GenericMessageEvent<PircBotX>> channelObs,
                 Observable<GenericMessageEvent<PircBotX>> privateObs,
                 GameOverListener gameOverListener) {
        userManager = new UserManager();
        joinManager = new JoinManager(channel, channelObs, this);
        dayManager = new DayManager(channel, channelObs, this);
        nightManager = new NightManager(channel, channelObs, privateObs, this);

        this.gameOverListener = gameOverListener;
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
        gameOverListener.gameOver();
    }

    @Override
    public void onDayEnd(Role role) {
        if (role != null) {
            userManager.killPlayer(role.getUser().getNick());
        }

        if (isGameOver(userManager.getPlayers())) {
            gameOverListener.gameOver();
        } else {
            nightManager.beginNight(userManager.getPlayers());
        }
    }

    @Override
    public void onNightEnd(List<Role> theMurdered) {
        for (Role deceased : theMurdered) {
            userManager.killPlayer(deceased.getUser().getNick());
        }
        if (isGameOver(userManager.getPlayers())) {
            gameOverListener.gameOver();
        } else {
            dayManager.beginDay(userManager.getPlayers());
        }
    }

    private boolean isGameOver(Map<String, Role> players) {
        if (players.isEmpty()) {
            channel.send().message("The game is over! No one wins");
            return true;
        }

        Map<Role.Faction, Integer> counts = new HashMap<>();

        for (Role.Faction faction : Role.Faction.values()) {
            counts.put(faction, 0);
        }

        players.values().forEach(role -> counts.put(role.getFaction(), counts.get(role.getFaction()) + 1));

        return counts.values().stream().filter(integer -> integer > 0).count() > 1;
    }
}
