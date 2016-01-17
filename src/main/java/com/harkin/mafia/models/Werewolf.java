package com.harkin.mafia.models;

import org.pircbotx.User;

public class Werewolf extends Role {

    public Werewolf(User user) {
        super(user);
    }

    @Override
    public String getNightActionText() {
        return "!kill";
    }

    @Override
    public int getPriority() {
        return PRIORITY_WEREWOLF;
    }

    @Override
    public Faction getFaction() {
        return Faction.WEREWOLF;
    }
}
