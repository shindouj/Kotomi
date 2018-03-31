package net.jeikobu.kotomi.malapi;

import com.thoughtworks.xstream.XStream;
import net.jeikobu.jfoundation.PropertiesManager;
import net.jeikobu.jfoundation.XStreamHelper;
import net.jeikobu.jfoundation.exceptions.UnsupportedConversionException;
import net.jeikobu.kotomi.KotomiMain;
import net.jeikobu.kotomi.KotomiPropertyKeys;
import org.pmw.tinylog.Logger;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;
import java.util.Optional;
import java.util.stream.Collectors;

public class MALConnector {
    public static Optional<AnimeList> searchForAnime(String animeName) {
        animeName = animeName.replace(' ', '+');

        PropertiesManager properties;
        try {
            properties = KotomiMain.getProperties();
        } catch (IOException e) {
            Logger.error(e);
            return Optional.empty();
        }

        try {
            String endpoint = properties.getValue(KotomiPropertyKeys.MAL_ANIME_ENDPOINT);
            String username = properties.getValue(KotomiPropertyKeys.MAL_LOGIN);
            String password = properties.getValue(KotomiPropertyKeys.MAL_PASSWORD);

            XStream xStream = XStreamHelper.getXStreamForClass(Anime.class);
            xStream.processAnnotations(AnimeList.class);

            String xml = getXML(endpoint, username, password, animeName);

            if (xml == null || xml.equals("/n") || xml.equals("")) return Optional.empty();

            return Optional.ofNullable((AnimeList) xStream.fromXML(xml));
        } catch (UnsupportedConversionException | IOException e) {
            Logger.error(e);
            return Optional.empty();
        }
    }

    public static Optional<MangaList> searchForManga(String mangaName) {
        mangaName = mangaName.replace(' ', '+');

        PropertiesManager properties;
        try {
            properties = KotomiMain.getProperties();
        } catch (IOException e) {
            Logger.error(e);
            return Optional.empty();
        }

        try {
            String endpoint = properties.getValue(KotomiPropertyKeys.MAL_MANGA_ENDPOINT);
            String username = properties.getValue(KotomiPropertyKeys.MAL_LOGIN);
            String password = properties.getValue(KotomiPropertyKeys.MAL_PASSWORD);

            XStream xStream = XStreamHelper.getXStreamForClass(Manga.class);
            xStream.processAnnotations(MangaList.class);

            String xml = getXML(endpoint, username, password, mangaName);

            Logger.debug(xml);

            if (xml == null || xml.equals("/n") || xml.equals("")) return Optional.empty();

            return Optional.ofNullable((MangaList) xStream.fromXML(xml));
        } catch (UnsupportedConversionException | IOException e) {
            Logger.error(e);
            return Optional.empty();
        }
    }

    private static String getXML(String endpoint, String username, String password, String query) {
        try {
            URL url = new URL(endpoint + "?q=" + query);
            String encodedCredentials = Base64.getEncoder()
                    .encodeToString((username + ":" + password)
                    .getBytes("UTF-8"));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Authorization", "Basic " + encodedCredentials);
            InputStream content = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(content));

            return in.lines().collect(Collectors.joining("\n"));
        } catch (IOException e) {
            Logger.error(e);
            return null;
        }
    }
}
