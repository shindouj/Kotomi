package net.jeikobu.kotomi.malapi;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("manga")
@XStreamConverter(MangaListConverter.class)
public class MangaList {
    @XStreamAlias("entry")
    public final List<Manga> mangaList;

    public MangaList() {
        this.mangaList = new ArrayList<>();
    }
}
