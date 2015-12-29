import mafia.GameManager;
import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.PrivateMessageEvent;

public class EventListener extends ListenerAdapter<PircBotX> {

    GameManager gameManager = new GameManager();

    @Override
    public void onMessage(MessageEvent<PircBotX> event) throws Exception {
        String message = event.getMessage();

        switch (message) {
            case "!start":
                gameManager.startGame(event.getChannel());
                break;
            case "!stop":
                //todo mafia fun never stops
                break;
            case "!join":
                gameManager.addPlayer(event.getUser());
                break;
            case "!help":
                event.respond("help to be written");
                break;
        }
    }

    @Override
    public void onPrivateMessage(PrivateMessageEvent<PircBotX> event) throws Exception {

    }
}
