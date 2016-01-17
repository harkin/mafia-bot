package com.harkin.mafia.models;

import org.pircbotx.User;

public class Bodyguard extends Role {

    public Bodyguard(User user) {
        super(user);
    }

    @Override
    public String getNightActionText() {
        return "!guard";
    }

    @Override
    public int getPriority() {
        return PRIORITY_BODYGUARD;
    }

    @Override
    public Faction getFaction() {
        return Faction.VILLAGE;
    }
}
