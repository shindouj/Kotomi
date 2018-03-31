package net.jeikobu.kotomi;

import net.jeikobu.jfoundation.PropertiesManager;
import net.jeikobu.jfoundation.PropertyKey;
import net.jeikobu.jfoundation.exceptions.UnsupportedConversionException;
import net.jeikobu.kotomi.listeners.MessageEventListener;
import net.jeikobu.kotomi.listeners.MinorEventListener;
import net.jeikobu.kotomi.malapi.MALConnector;
import org.pmw.tinylog.Logger;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;

import java.io.IOException;
import java.util.List;

public class KotomiMain {
    // properties key list
    private static final List<PropertyKey<?>> propertyKeyList = new KotomiPropertyKeys().getPropertyList();

    // properties manager singleton
    private static PropertiesManager manager;

    public static PropertiesManager getProperties() throws IOException {
        if (manager == null) {
            manager = new PropertiesManager(propertyKeyList);
        }
        return manager;
    }

    public static void main(String... args) {
        String discordToken;

        try {
            Logger.debug(propertyKeyList);
            discordToken = getProperties().getValue(KotomiPropertyKeys.DISCORD_TOKEN);
        } catch (UnsupportedConversionException | IOException e) {
            Logger.error(e);
            return;
        }

        IDiscordClient discordClient = new ClientBuilder()
                .withToken(discordToken)
                .withRecommendedShardCount(true)
                .registerListener(new MessageEventListener())
                .registerListener(new MinorEventListener())
                .build();

        discordClient.login();

        MALConnector.searchForManga("Bonnouji");
    }
}
