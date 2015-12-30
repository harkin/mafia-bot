package com.harkin;

import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.managers.ThreadedListenerManager;
import rx.subjects.BehaviorSubject;

public class MyThreadManager extends ThreadedListenerManager<PircBotX> {

//    private final ListenerAdapter dayListener;
//    private final ListenerAdapter nightListener;
//    private final ListenerAdapter joinListener;
    private final ListenerAdapter idleListener;

    private final BehaviorSubject

//    public MyThreadManager(ListenerAdapter dayListener, ListenerAdapter nightListener,
//                           ListenerAdapter joinListener, ListenerAdapter idleListener) {
//        super();
//        this.dayListener = dayListener;
//        this.nightListener = nightListener;
//        this.joinListener = joinListener;
//        this.idleListener = idleListener;
//
//        beginIdling();
//    }

    public MyThreadManager(ListenerAdapter idleListener){
        super();

        this.idleListener = idleListener;

        addListener(idleListener);
    }


//    public void beginDay() {
//        getListenersReal().clear();
//        addListener(dayListener);
//    }
//
//    public void beginNight() {
//        getListenersReal().clear();
//        addListener(nightListener);
//    }
//
//    public void beginJoining() {
//        getListenersReal().clear();
//        addListener(joinListener);
//    }
//
//    public void beginIdling() {
//        getListenersReal().clear();
//        addListener(idleListener);
//    }
}
