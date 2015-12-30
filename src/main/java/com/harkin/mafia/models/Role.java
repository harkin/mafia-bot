package com.harkin.mafia.models;

import org.pircbotx.User;

public abstract class Role {

    protected final User user;

    public Role(User user) {
        this.user = user;
    }

    public abstract boolean hasNightAction();

    public abstract String getInspectionText();

}
