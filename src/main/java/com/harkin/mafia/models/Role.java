package com.harkin.mafia.models;

import org.pircbotx.User;

public abstract class Role {

    public enum Faction {
        VILLAGE, MAFIA, WEREWOLF
    }

    public static final int PRIORITY_HOOKER = 0;
    public static final int PRIORITY_BODYGUARD = 1;
    public static final int PRIORITY_WEREWOLF = 2;
    public static final int PRIORITY_MAFIA = 3;
    public static final int PRIORITY_INSPECTOR = 4;
    public static final int PRIORITY_SILENCER = 5;

    protected final User user;

    public Role(User user) {
        this.user = user;
    }

    public abstract String getInspectionText();

    public abstract String getNightActionText();

    public abstract int getPriority();

    public abstract Faction getFaction();

    public final User getUser() {
        return user;
    }
}
