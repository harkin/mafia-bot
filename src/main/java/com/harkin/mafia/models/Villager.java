package com.harkin.mafia.models;

import org.pircbotx.User;

public class Villager extends Role {

    public Villager(User user) {
        super(user);
    }

    @Override
    public boolean hasNightAction() {
        return false;
    }

    @Override
    public String getInspectionText() {
        return String.format("%s is a common villager", user.getNick());
    }
}
