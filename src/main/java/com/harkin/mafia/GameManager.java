package com.harkin.mafia;

import com.harkin.ObsListener;
import org.pircbotx.Channel;
import org.pircbotx.User;
import rx.Subscription;

public class GameManager {

    private final ObsListener obsListener;

    private Mafia mafia;
    private Subscription idleSub;

    public GameManager(ObsListener obsListener) {
        this.obsListener = obsListener;
    }

    public void start() {
        idleSub = obsListener.getChannelObs()
                .filter(event -> event.getMessage().startsWith("!"))
                .filter(event -> event.getMessage().toLowerCase().equals("!start"))
                .subscribe(event -> {
                    onGameStarted(event.getUser(), event.getChannel());
                    event.respond("Starting a new game of mafia. Type \"!join\" to take part. The game will start in 45 seconds");
                });
    }

    public void onGameStarted(User user, Channel channel) {
        idleSub.unsubscribe();
        mafia = new Mafia(channel, obsListener.getChannelObs(), obsListener.getPrivateObs());
        mafia.start(user);
    }

    public void onGameEnded() {
        mafia = null;
        start();
    }
}
