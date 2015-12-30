package com.harkin.mafia;

import org.pircbotx.User;

import java.util.List;

public interface OnJoinInteraction {
    void onPlayersJoined(List<User> players);

    void onGameCancelled();
}
