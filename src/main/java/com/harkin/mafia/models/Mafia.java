package com.harkin.mafia.models;

import org.pircbotx.User;

public class Mafia extends Role {

    public Mafia(User user) {
        super(user);
    }

    @Override
    public String getNightActionText() {
        return "!kill";
    }

    @Override
    public int getPriority() {
        return PRIORITY_MAFIA;
    }

    @Override
    public Faction getFaction() {
        return Faction.MAFIA;
    }
}
