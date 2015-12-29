package mafia;

import mafia.models.Role;
import mafia.models.Villager;
import org.pircbotx.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserManager {
    private final Set<User> joiners = new HashSet<>();
    private final List<Role> players = new ArrayList<>();

    public void addUser(User user) {
        joiners.add(user);
    }

    public void assignRoles() {
        for (User user : joiners) {
            players.add(new Villager(user));
        }

        joiners.clear();
    }

    public List<Role> getPlayers() {
        return players;
    }
}
