package net.jeikobu.kotomi;

import net.jeikobu.jfoundation.PropertyKey;
import org.pmw.tinylog.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

// TODO: Construct PropertyKeys with an XML stencil

public class KotomiPropertyKeys {
    public static final PropertyKey<String> DISCORD_TOKEN = new PropertyKey<>("discordToken", "", String.class);
    public static final PropertyKey<String> MAL_LOGIN = new PropertyKey<>("malLogin", "", String.class);
    public static final PropertyKey<String> MAL_PASSWORD = new PropertyKey<>("malPassword", "", String.class);
    public static final PropertyKey<String> MAL_ANIME_ENDPOINT = new PropertyKey<>("malAnimeEndpoint",
            "https://myanimelist.net/api/anime/search.xml", String.class);
    public static final PropertyKey<String> MAL_MANGA_ENDPOINT = new PropertyKey<>("malMangaEndpoint",
            "https://myanimelist.net/api/manga/search.xml", String.class);

    public List<PropertyKey<?>> getPropertyList() {
        List<PropertyKey<?>> propertyKeyList = new ArrayList<>();

        for (Field f: getClass().getFields()) {
            if (Modifier.isStatic(f.getModifiers()) && Modifier.isPublic(f.getModifiers()) && Modifier.isFinal(f.getModifiers())) {
                if (f.getType().equals(PropertyKey.class)) {
                    try {
                        Logger.debug(f.get(this));
                        propertyKeyList.add(((PropertyKey<?>) f.get(this)));
                    } catch (IllegalAccessException e) {
                        Logger.error(e);
                    }
                }
            }
        }
        return propertyKeyList;
    }
}
