package com.harkin.mafia.models;

import org.pircbotx.User;

public class Mayor extends Role {

    public Mayor(User user) {
        super(user);
    }

    @Override
    public String getNightActionText() {
        return null;
    }

    @Override
    public int getPriority() {
        return 0;
    }

    @Override
    public Faction getFaction() {
        return null;
    }
}
