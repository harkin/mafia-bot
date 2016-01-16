package com.harkin.mafia;

import com.harkin.ObsListener;
import org.pircbotx.Channel;
import org.pircbotx.User;
import org.pircbotx.hooks.events.MessageEvent;
import rx.Subscription;

public class Godfather {

    private final ObsListener obsListener;

    private Mafia mafia;
    private Subscription idleSub;

    public Godfather(ObsListener obsListener) {
        this.obsListener = obsListener;
    }

    public void start() {
        idleSub = obsListener.getChannelObs()
                .filter(event -> event.getMessage().startsWith("!"))
                .filter(event -> event.getMessage().toLowerCase().equals("!start"))
                .subscribe(event -> {
                    onGameStarted(event.getUser(), ((MessageEvent) event).getChannel());
                });
    }

    public void onGameStarted(User user, Channel channel) {
        idleSub.unsubscribe();
        idleSub = null;

        mafia = new Mafia(channel, obsListener.getChannelObs(), obsListener.getPrivateObs());
        //todo needs a way to let me know the game has ended
        mafia.start(user);
    }


    public void onGameEnded() {
        mafia = null;
        start();
    }
}
