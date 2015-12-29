import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;
import org.pircbotx.hooks.ListenerAdapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class Main {
    public static void main(String[] args) throws IOException, IrcException {
        System.out.println("Hello, world!");

        Reader propertyReader = new BufferedReader(new FileReader("bot.properties"));
        Properties botProperties = new Properties();
        botProperties.load(propertyReader);

        //required properties
        String botName = botProperties.getProperty("botName");
        String serverHostname = botProperties.getProperty("serverHostname");
        int serverPort = Integer.parseInt(botProperties.getProperty("serverPort"));

        //optional properties
        String autoJoinChannel = botProperties.getProperty("autoJoinChannel", "");
        String serverPassword = botProperties.getProperty("serverPassword", "");

        ListenerAdapter l = new EventListener();

        Configuration<PircBotX> configuration = new Configuration.Builder<>()
                .setName(botName)
                .setServerHostname(serverHostname)
                .setServerPort(serverPort)
                .setServerPassword(serverPassword)
                .addAutoJoinChannel(autoJoinChannel)
                .setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates()) //todo be less trustful
                .setListenerManager(new MyThreadManager(l, l, l, l))
                .buildConfiguration();

        PircBotX bot = new PircBotX(configuration);
        bot.startBot();
    }
}
