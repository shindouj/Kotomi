package net.jeikobu.kotomi.malapi;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("anime")
@XStreamConverter(AnimeListConverter.class)
public class AnimeList {
    @XStreamAlias("entry")
    public final List<Anime> animeList;

    public AnimeList() {
        this.animeList = new ArrayList<>();
    }
}
