package com.harkin.mafia.models;

import org.pircbotx.User;

public class Inspector extends Role {

    public Inspector(User user) {
        super(user);
    }

    @Override
    public String getNightActionText() {
        return "!inspect";
    }

    @Override
    public int getPriority() {
        return PRIORITY_INSPECTOR;
    }

    @Override
    public Faction getFaction() {
        return Faction.VILLAGE;
    }
}
