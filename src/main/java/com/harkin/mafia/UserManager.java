package com.harkin.mafia;

import com.google.common.base.Joiner;
import com.harkin.mafia.models.*;
import com.harkin.mafia.models.Mafia;
import org.pircbotx.Channel;
import org.pircbotx.User;
import rx.Observable;
import rx.exceptions.Exceptions;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class UserManager {

    private static final List<Class<?>> SMALL = Arrays.asList(Inspector.class, Hooker.class, Bodyguard.class, Mafia.class, Mafia.class, Villager.class, Villager.class, Mayor.class);

    //todo role selections for not small games
    private static final List<Class<?>> MEDIUM = Arrays.asList(Inspector.class, Hooker.class, Bodyguard.class, Mafia.class, Mafia.class, Villager.class, Villager.class, Mayor.class);
    private static final List<Class<?>> LARGE = Arrays.asList(Inspector.class, Hooker.class, Bodyguard.class, Mafia.class, Mafia.class, Villager.class, Villager.class, Mayor.class);

    private final Map<String, Role> players = new HashMap<>();

    public void assignRoles(List<User> users, Channel channel) {
        Collections.shuffle(users);

        Observable<Role> obs;
        if (users.size() <= SMALL.size()) {
            obs = getSmallGameRoles(users);
        } else if (users.size() <= MEDIUM.size()) {
            obs = getMediumGameRoles(users);
        } else {
            obs = getLargeGameRoles(users);
        }

        obs.subscribe(role -> players.put(role.getUser().getNick(), role),
                throwable -> {/* todo */},
                () -> channel.send().message("wheee"));

        String roles = Joiner.on(" ").join(players.values());
        channel.send().message(String.format("The roles are: %s", roles));
    }

    public void killPlayer(String username) {
        players.remove(username);
    }

    public Map<String, Role> getPlayers() {
        return players;
    }

    private static Observable<Role> getSmallGameRoles(List<User> players) {
        return Observable.from(SMALL)
                .zipWith(players, (aClass, user) -> {
                    try {
                        return (Role) aClass.getConstructor(User.class).newInstance(user);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }

    private static Observable<Role> getMediumGameRoles(List<User> players) {
        return Observable.from(MEDIUM)
                .zipWith(players, (aClass, user) -> {
                    try {
                        return (Role) aClass.getConstructor(User.class).newInstance(user);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }

    private static Observable<Role> getLargeGameRoles(List<User> players) {
        return Observable.from(LARGE)
                .zipWith(players, (aClass, user) -> {
                    try {
                        return (Role) aClass.getConstructor(User.class).newInstance(user);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw Exceptions.propagate(e);
                    }
                });
    }
}
