package net.jeikobu.kotomi.malapi;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class MangaListConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        throw new ConversionException("Cannot marshal this object back to XML!");
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext unmarshallingContext) {
        MangaList list = new MangaList();
        while(reader.hasMoreChildren()) {
            reader.moveDown();
            Manga m = (Manga) unmarshallingContext.convertAnother(list, Manga.class);
            list.mangaList.add(m);
            reader.moveUp();
        }
        return list;
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(MangaList.class);
    }
}
