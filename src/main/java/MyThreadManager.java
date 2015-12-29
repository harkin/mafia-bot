import org.pircbotx.PircBotX;
import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.managers.ThreadedListenerManager;

public class MyThreadManager extends ThreadedListenerManager<PircBotX> {

    private final ListenerAdapter dayListener;
    private final ListenerAdapter nightListener;
    private final ListenerAdapter joinListener;
    private final ListenerAdapter idleListener;

    public MyThreadManager(ListenerAdapter dayListener, ListenerAdapter nightListener,
                           ListenerAdapter joinListener, ListenerAdapter idleListener) {
        super();
        this.dayListener = dayListener;
        this.nightListener = nightListener;
        this.joinListener = joinListener;
        this.idleListener = idleListener;

        addListener(idleListener);
    }
}
