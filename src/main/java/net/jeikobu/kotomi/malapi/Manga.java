package net.jeikobu.kotomi.malapi;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import net.jeikobu.jfoundation.exceptions.ElementNotFoundException;
import org.apache.commons.text.StringEscapeUtils;
import org.pmw.tinylog.Logger;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.util.Optional;

@XStreamConverter(MangaConverter.class)
@XStreamAlias("entry")
public class Manga {
    private final int id;
    private final String title;
    private final String englishTitle;
    private final String synonyms;
    private final int chapters;
    private final int volumes;
    private final BigDecimal score;
    private final String type;
    private final String status;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String synopsis;
    private final URI imageURL;

    private Manga(MangaBuilder b) {
        this.id = b.id;
        this.title = b.title;

        if (b.englishTitle.equals("") || b.englishTitle.equals("\n")) {
            this.englishTitle = null;
        } else {
            this.englishTitle = b.englishTitle;
        }

        if (b.synonyms.equals("") || b.synonyms.equals("\n")) {
            this.synonyms = null;
        } else {
            this.synonyms = b.synonyms;
        }

        this.chapters = b.chapters;
        this.volumes = b.volumes;
        this.type = b.type;
        this.status = b.status;
        this.score = b.score;
        this.startDate = b.startDate;
        this.endDate = b.endDate;
        this.synopsis = StringEscapeUtils.unescapeHtml4(b.synopsis)
                .replace("<br />", "")
                .replace("[i]", "*")
                .replace("[/i]", "*")
                .replace("[b]", "**")
                .replace("[/b]", "**");
        this.imageURL = b.imageURL;

    }

    public enum MangaTags {
        ID("id"),
        TITLE("title"),
        ENGLISH_TITLE("english"),
        SYNONYMS("synonyms"),
        CHAPTERS("chapters"),
        VOLUMES("volumes"),
        TYPE("type"),
        STATUS("status"),
        START_DATE("start_date"),
        END_DATE("end_date"),
        SYNOPSIS("synopsis"),
        SCORE("score"),
        IMAGE_URL("image");

        private String name;

        MangaTags(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static MangaTags byTagName(String tagName) throws ElementNotFoundException {
            for (MangaTags tag: MangaTags.values()) {
                if (tag.getName().equals(tagName)) return tag;
            }
            throw new ElementNotFoundException("Unknown element: " + tagName + "!");
        }
    }

    public static final class MangaBuilder {
        private int id;
        private String title;
        private String englishTitle;
        private String synonyms;
        private int chapters;
        private int volumes;
        private BigDecimal score;
        private String type;
        private String status;
        private LocalDate startDate;
        private LocalDate endDate;
        private String synopsis;
        private URI imageURL;

        public MangaBuilder() {
        }

        public MangaBuilder id(String id) {
            this.id = Integer.parseInt(id);
            return this;
        }

        public MangaBuilder title(String title) {
            this.title = title;
            return this;
        }

        public MangaBuilder englishTitle(String englishTitle) {
            this.englishTitle = englishTitle;
            return this;
        }

        public MangaBuilder synonyms(String synonyms) {
            this.synonyms = synonyms;
            return this;
        }

        public MangaBuilder chapters(int chapters) {
            this.chapters = chapters;
            return this;
        }

        public MangaBuilder volumes(int volumes) {
            this.volumes = volumes;
            return this;
        }

        public MangaBuilder score(String score) {
            this.score = new BigDecimal(score);
            return this;
        }

        public MangaBuilder type(String type) {
            this.type = type;
            return this;
        }

        public MangaBuilder status(String status) {
            this.status = status;
            return this;
        }

        public MangaBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public MangaBuilder startDate(String startDate) {
            this.startDate = LocalDate.parse(startDate);
            return this;
        }

        public MangaBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public MangaBuilder endDate(String endDate) {
            this.endDate = LocalDate.parse(endDate);
            return this;
        }

        public MangaBuilder synopsis(String synopsis) {
            this.synopsis = synopsis;
            return this;
        }

        public MangaBuilder imageURL(String imageURL) throws URISyntaxException {
            this.imageURL = new URI(imageURL);
            return this;
        }

        public Manga build() {
            return new Manga(this);
        }
    }

    public int getId() {
        return id;
    }

    public Optional<String> getTitle() {
        return Optional.ofNullable(title);
    }

    public Optional<String> getEnglishTitle() {
        return Optional.ofNullable(englishTitle);
    }

    public Optional<String> getSynonyms() {
        return Optional.ofNullable(synonyms);
    }

    public int getChapters() {
        return chapters;
    }

    public int getVolumes() {
        return volumes;
    }

    public Optional<BigDecimal> getScore() {
        return Optional.ofNullable(score);
    }

    public Optional<String> getType() {
        return Optional.ofNullable(type);
    }

    public Optional<String> getStatus() {
        return Optional.ofNullable(status);
    }

    public Optional<LocalDate> getStartDate() {
        return Optional.ofNullable(startDate);
    }

    public Optional<LocalDate> getEndDate() {
        return Optional.ofNullable(endDate);
    }

    public Optional<String> getSynopsis() {
        return Optional.ofNullable(synopsis);
    }

    public Optional<URI> getImageURL() {
        return Optional.ofNullable(imageURL);
    }

    public Optional<EmbedObject> toEmbed() {
        try {
            EmbedBuilder builder = new EmbedBuilder();

            builder.withColor(Color.getHSBColor(270.7f, 72.6f, 65.7f));

            builder.withAuthorName("Kotomi");
            builder.withUrl("https://myanimelist.net/manga/" + id);
            builder.withTitle(title);

            if (getEnglishTitle().isPresent()) {
                builder.appendDesc("English title: " + englishTitle + "\n");
            }

            if (getSynonyms().isPresent()) {
                builder.appendDesc("Alternative titles: " + synonyms);
            }

            builder.appendField("Type", type, true);

            if (getStatus().isPresent()) {
                builder.appendField("Status", status, true);
            }

            builder.appendField("Ch/Vol", Integer.toString(chapters) + "/" + Integer.toString(volumes), true);

            builder.appendField("Avg. Score", score.toPlainString(), true);

            if (getStartDate().isPresent()) {
                builder.appendField("Start Date", startDate.toString(), true);
            }

            if (getEndDate().isPresent()) {
                builder.appendField("End Date", endDate.toString(), true);
            }

            if (synopsis.length() > 256) {
                builder.appendField("Description", synopsis.substring(0, 252) + "...", false);
            } else {
                builder.appendField("Description", synopsis, false);
            }

            if (title.equals("Nichijou")) {
                builder.withFooterText("WE NEED SEASON 2 :C");
            }

            if (getImageURL().isPresent()) {
                builder.withImage(imageURL.toString());
            }

            return Optional.of(builder.build());
        } catch (Exception e) {
            Logger.error(e);
            return Optional.empty();
        }
    }

}
