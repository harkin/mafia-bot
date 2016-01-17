package com.harkin.mafia.models;

import org.pircbotx.User;

public class Hooker extends Role {

    public Hooker(User user) {
        super(user);
    }

    @Override
    public String getNightActionText() {
        return "!hook";
    }

    @Override
    public int getPriority() {
        return PRIORITY_HOOKER;
    }

    @Override
    public Faction getFaction() {
        return Faction.VILLAGE;
    }
}
