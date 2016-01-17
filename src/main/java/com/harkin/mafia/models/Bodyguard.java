package com.harkin.mafia.models;

public class Bodyguard extends Role {
    @Override
    public String getInspectionText() {
        return null;
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
