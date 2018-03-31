package net.jeikobu.kotomi.malapi;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.pmw.tinylog.Logger;

import java.time.format.DateTimeParseException;

public class MangaConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter hierarchicalStreamWriter, MarshallingContext marshallingContext) {
        throw new ConversionException("Cannot marshal this object back to XML!");
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Manga.MangaBuilder builder = new Manga.MangaBuilder();

        while (reader.hasMoreChildren()) {
            try {


                reader.moveDown();
                Manga.MangaTags tag = Manga.MangaTags.byTagName(reader.getNodeName());
                switch (tag) {
                    case ID: {
                        builder = builder.id(reader.getValue());
                        break;
                    }
                    case TITLE: {
                        builder = builder.title(reader.getValue());
                        break;
                    }
                    case SCORE: {
                        builder = builder.score(reader.getValue());
                        break;
                    }
                    case START_DATE: {
                        try {
                            builder = builder.startDate(reader.getValue());
                        } catch (DateTimeParseException e) {
                            Logger.error(e);
                        }
                        break;
                    }
                    case END_DATE: {
                        try {
                            builder = builder.endDate(reader.getValue());
                        } catch (DateTimeParseException e) {
                            Logger.error(e);
                        }
                        break;
                    }
                    case TYPE: {
                        builder = builder.type(reader.getValue());
                        break;
                    }
                    case STATUS: {
                        builder = builder.status(reader.getValue());
                        break;
                    }
                    case SYNONYMS: {
                        builder = builder.synonyms(reader.getValue());
                        break;
                    }
                    case CHAPTERS: {
                        builder = builder.chapters(Integer.parseInt(reader.getValue()));
                        break;
                    }
                    case VOLUMES: {
                        builder = builder.volumes(Integer.parseInt(reader.getValue()));
                        break;
                    }
                    case SYNOPSIS: {
                        builder = builder.synopsis(reader.getValue());
                        break;
                    }
                    case IMAGE_URL: {
                        builder = builder.imageURL(reader.getValue());
                        break;
                    }
                    case ENGLISH_TITLE: {
                        builder = builder.englishTitle(reader.getValue());
                        break;
                    }
                }
                reader.moveUp();
            } catch (Exception e) {
                throw new ConversionException(e);
            }
        }

        return builder.build();
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Manga.class);
    }
}
