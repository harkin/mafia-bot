package com.harkin.mafia;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class DayManager extends ListenerAdapter<PircBotX> {

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        if (isVote(event.getMessage())) {
            event.respond("All voting much take place in public");
        }
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception {
        if (isVote(event.getMessage())) {
            event.respond("All voting much take place in public");
        }
    }

    public void endDay(){

    }










    private static boolean isVote(String message) {
        return message.startsWith("!") && message.toLowerCase().startsWith("!vote") && message.split(" ").length == 2;
    }
}
