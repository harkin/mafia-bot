package com.harkin;

import com.harkin.mafia.Godfather;
import org.pircbotx.Configuration;
import org.pircbotx.PircBotX;
import org.pircbotx.UtilSSLSocketFactory;
import org.pircbotx.exception.IrcException;

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

        ObsListener listener = new ObsListener();
        Godfather gm = new Godfather(listener);
        gm.start();

        Configuration<PircBotX> configuration = new Configuration.Builder<>()
                .setName(botName)
                .setServerHostname(serverHostname)
                .setServerPort(serverPort)
                .setServerPassword(serverPassword)
                .addAutoJoinChannel(autoJoinChannel)
                .setSocketFactory(new UtilSSLSocketFactory().trustAllCertificates()) //todo be less trustful
                .addListener(listener)
                .buildConfiguration();

        PircBotX bot = new PircBotX(configuration);
        bot.startBot();
    }
}
