package com.harkin.mafia.models;

import org.pircbotx.User;

public abstract class Role {

    public static final int PRIORITY_HOOKER = 0;
    public static final int PRIORITY_BODYGUARD = 1;
    public static final int PRIORITY_WEREWOLF = 2;
    public static final int PRIORITY_MAFIA = 3;
    public static final int PRIORITY_INSPECTOR = 4;
    public static final int PRIORITY_SILENCER = 5;
//    public static final int PRIORITY_HOOKER = 5;
//    public static final int PRIORITY_HOOKER = 6;
//    public static final int PRIORITY_HOOKER = 7;
//    public static final int PRIORITY_HOOKER = 8;

    protected final User user;
    private int lastNightProected = -1;

    public Role(User user) {
        this.user = user;
    }

    public abstract boolean hasNightAction();

    public abstract String getInspectionText();

    public abstract String getNightActionText();

    public abstract int getPriority();

    public void setProtected(int night) {
        lastNightProected = night;
    }

    public boolean isProectedTonight(int night) {
        return lastNightProected == night;
    }

    public final User getUser() {
        return user;
    }
}
