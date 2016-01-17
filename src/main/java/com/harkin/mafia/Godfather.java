package com.harkin.mafia;

import com.harkin.ObsListener;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import rx.Subscription;

public class Godfather implements GameOverListener {

    private final ObsListener obsListener;

    private Mafia mafia;
    private Subscription idleSub;

    public Godfather(ObsListener obsListener) {
        this.obsListener = obsListener;
    }

    public void start() {
        idleSub = obsListener.getChannelObs()
                .filter(event -> event.getMessage().startsWith("!"))
                .filter(event -> event.getMessage().toLowerCase().equals("!mafia"))
                .subscribe(event -> {
                    startGame(event.getUser(), ((MessageEvent) event).getChannel());
                });
    }

    public void startGame(User user, Channel channel) {
        idleSub.unsubscribe();
        idleSub = null;

        mafia = new Mafia(channel, obsListener.getChannelObs(), obsListener.getPrivateObs(), this);

        mafia.start(user);
    }

    @Override
    public void gameOver() {
        mafia = null;
        start();
    }
}
