package net.jeikobu.kotomi.malapi;

import com.thoughtworks.xstream.converters.ConversionException;
import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import org.pmw.tinylog.Logger;

import java.time.format.DateTimeParseException;

public class AnimeConverter implements Converter {
    @Override
    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext context) {
        throw new ConversionException("Cannot marshal this object back to XML!");
    }

    @Override
    public Anime unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        try {
            Anime.AnimeBuilder builder = new Anime.AnimeBuilder();

            while(reader.hasMoreChildren()) {
                reader.moveDown();
                Anime.AnimeTags tag = Anime.AnimeTags.byTagName(reader.getNodeName());
                switch(tag) {
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
                    case EPISODES: {
                        builder = builder.episodes(reader.getValue());
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
            }

            return builder.build();
        } catch (Exception e) {
            throw new ConversionException(e);
        }
    }

    @Override
    public boolean canConvert(Class aClass) {
        return aClass.equals(Anime.class);
    }
}
