package mafia;

import org.pircbotx.Channel;
import org.pircbotx.User;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class GameManager {

    private static final int IDLE = 0;
    private static final int JOINING = 1;
    private static final int NIGHT = 2;
    private static final int DAY = 3;

    private final UserManager userManager = new UserManager();
    private final DayManager dayManager = new DayManager();
    private final NightManager nightManager = new NightManager();

    private int currentPhase = IDLE;

    public void startGame(Channel channel) {
        currentPhase = JOINING;
        channel.send().message("Starting a new game of mafia. Type \"!join\" to take part. The game will start in 45 seconds");
        Executors.newSingleThreadScheduledExecutor().schedule(() -> beginNight(channel), 45, TimeUnit.SECONDS);
    }

    public void addPlayer(User user) {
        if (currentPhase == JOINING) {
            userManager.addUser(user);
        }
        //todo error text saying whether game is ongoing or not started
    }


    private void setupGame(Channel channel) {
        currentPhase = NIGHT;
        userManager.assignRoles();
        beginNight(channel);
    }

    private void beginNight(Channel channel) {
        currentPhase = NIGHT;
        channel.send().message(String.format("Night %d begins. You have 30 seconds to perform a night action.", nightManager.getNightCount()));
    }

    private void beginDay(Channel channel) {
        currentPhase = DAY;
    }
}
