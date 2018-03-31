package net.jeikobu.kotomi.malapi;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class AnimeListConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        throw new ConversionException("Cannot marshal this object back to XML!");
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
        AnimeList list = new AnimeList();
        while(reader.hasMoreChildren()) {
            reader.moveDown();
            Anime a = (Anime) unmarshallingContext.convertAnother(list, Anime.class);
            list.animeList.add(a);
            reader.moveUp();
        }
        return list;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(AnimeList.class);
    }
}
