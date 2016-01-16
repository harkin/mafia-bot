package com.harkin;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class ObsListener extends ListenerAdapter<PircBotX> {

    private final BehaviorSubject<GenericMessageEvent<PircBotX>> channelMessage = BehaviorSubject.create();
    private final BehaviorSubject<GenericMessageEvent<PircBotX>> privateMessage = BehaviorSubject.create();

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        channelMessage.onNext(event);
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception {
        privateMessage.onNext(event);
    }

    public Observable<GenericMessageEvent<PircBotX>> getChannelObs() {
        return channelMessage.asObservable();
    }

    public Observable<GenericMessageEvent<PircBotX>> getPrivateObs() {
        return privateMessage.asObservable();
    }
}
