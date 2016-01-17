package com.harkin.mafia.models;

import org.pircbotx.User;

public class Villager extends Role {

    public Villager(User user) {
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
        return Faction.VILLAGE;
    }
}
