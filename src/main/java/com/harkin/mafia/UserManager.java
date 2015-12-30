package com.harkin.mafia;

import com.harkin.mafia.models.Role;
import com.harkin.mafia.models.Villager;
import org.pircbotx.Channel;
import org.pircbotx.User;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
    private final List<Role> players = new ArrayList<>();

    public void assignRoles(List<User> users, Channel channel) {

        //todo give each user a random role
        //todo some way to pick which roles are in the game based on the number of users
        for (User user : users) {
            players.add(new Villager(user));
        }

        //todo announce what roles are in the game
    }

    public List<Role> getPlayers() {
        return players;
    }
}
