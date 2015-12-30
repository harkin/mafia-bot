package com.harkin;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;
import rx.Observable;
import rx.subjects.BehaviorSubject;

public class ObsListener extends ListenerAdapter<PircBotX> {

    private final BehaviorSubject<MessageEvent<PircBotX>> channelMessage = BehaviorSubject.create();
    private final BehaviorSubject<PrivateMessageEvent<PircBotX>> privateMessage = BehaviorSubject.create();

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        channelMessage.onNext(event);
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception {
        privateMessage.onNext(event);
    }

    public Observable<MessageEvent<PircBotX>> getChannelObs() {
        return channelMessage.asObservable();
    }

    public Observable<PrivateMessageEvent<PircBotX>> getPrivateObs() {
        return privateMessage.asObservable();
    }
}
