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

@XStreamConverter(AnimeConverter.class)
@XStreamAlias("entry")
public class Anime {
    private final int id;
    private final String title;
    private final String englishTitle;
    private final String synonyms;
    private final int episodes;
    private final AnimeType type;
    private final String status;
    private final BigDecimal score;
    private final LocalDate startDate;
    private final LocalDate endDate;
    private final String synopsis;
    private final URI imageURL;

    private Anime(AnimeBuilder b) {
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

        this.episodes = b.episodes;
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

    public enum AnimeTags {
        ID("id"),
        TITLE("title"),
        ENGLISH_TITLE("english"),
        SYNONYMS("synonyms"),
        EPISODES("episodes"),
        TYPE("type"),
        STATUS("status"),
        START_DATE("start_date"),
        END_DATE("end_date"),
        SYNOPSIS("synopsis"),
        SCORE("score"),
        IMAGE_URL("image");

        private final String name;

        AnimeTags(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public static AnimeTags byTagName(String tagName) throws ElementNotFoundException {
            for (AnimeTags tag: AnimeTags.values()) {
                if (tag.getName().equals(tagName)) return tag;
            }
            throw new ElementNotFoundException("Unknown element: " + tagName + "!");
        }
    }

    public static class AnimeBuilder {
        private int id;
        private String title;
        private String englishTitle;
        private String synonyms;
        private int episodes;
        private AnimeType type;
        private String status;
        private BigDecimal score;
        private LocalDate startDate;
        private LocalDate endDate;
        private String synopsis;
        private URI imageURL;

        public AnimeBuilder() { }

        public AnimeBuilder id(String id) {
            this.id = Integer.parseInt(id);
            return this;
        }

        public AnimeBuilder title(String title) {
            this.title = title;
            return this;
        }

        public AnimeBuilder episodes(String episodes) {
            this.episodes = Integer.parseInt(episodes);
            return this;
        }

        public AnimeBuilder englishTitle(String englishTitle) {
            this.englishTitle = englishTitle;
            return this;
        }

        public AnimeBuilder synonyms(String synonyms) {
            this.synonyms = synonyms;
            return this;
        }

        public AnimeBuilder type(String type) {
            this.type = AnimeType.valueOf(type.toUpperCase());
            return this;
        }

        public AnimeBuilder type(AnimeType type) {
            this.type = type;
            return this;
        }

        public AnimeBuilder status(String status) {
            this.status = status;
            return this;
        }

        public AnimeBuilder score(String score) {
            this.score = new BigDecimal(score);
            return this;
        }

        public AnimeBuilder startDate(String startDate) {
            this.startDate = LocalDate.parse(startDate);
            return this;
        }

        public AnimeBuilder startDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public AnimeBuilder endDate(String endDate) {
            this.endDate = LocalDate.parse(endDate);
            return this;
        }

        public AnimeBuilder endDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public AnimeBuilder synopsis(String synopsis) {
            this.synopsis = synopsis;
            return this;
        }

        public AnimeBuilder imageURL(String imageURL) throws URISyntaxException {
            this.imageURL = new URI(imageURL);
            return this;
        }

        public Anime build() {
            return new Anime(this);
        }
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public Optional<String> getEnglishTitle() {
        return Optional.ofNullable(englishTitle);
    }

    public Optional<String> getSynonyms() {
        return Optional.ofNullable(synonyms);
    }

    public int getEpisodes() {
        return episodes;
    }

    public AnimeType getType() {
        return type;
    }

    public Optional<String> getStatus() {
        return Optional.ofNullable(status);
    }

    public BigDecimal getScore() {
        return score;
    }

    public Optional<LocalDate> getStartDate() {
        return Optional.ofNullable(startDate);
    }

    public Optional<LocalDate> getEndDate() {
        return Optional.ofNullable(endDate);
    }

    public String getSynopsis() {
        return synopsis;
    }

    public Optional<URI> getImageURL() {
        return Optional.ofNullable(imageURL);
    }

    public Optional<EmbedObject> toEmbed() {
        try {
            EmbedBuilder builder = new EmbedBuilder();

            builder.withColor(Color.getHSBColor(270.7f, 72.6f, 65.7f));

            builder.withAuthorName("Kotomi");
            builder.withUrl("https://myanimelist.net/anime/" + id);
            builder.withTitle(title);

            if (getEnglishTitle().isPresent()) {
                builder.appendDesc("English title: " + englishTitle + "\n");
            }

            if (getSynonyms().isPresent()) {
                builder.appendDesc("Alternative titles: " + synonyms);
            }

            builder.appendField("Type", type.name(), true);

            if (getStatus().isPresent()) {
                builder.appendField("Status", status, true);
            }

            builder.appendField("Episode count", Integer.toString(episodes), true);

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

    @Override
    public String toString() {
        return com.google.common.base.MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("englishTitle", englishTitle)
                .add("synonyms", synonyms)
                .add("episodes", episodes)
                .add("type", type)
                .add("status", status)
                .add("score", score)
                .add("startDate", startDate)
                .add("endDate", endDate)
                .add("synopsis", synopsis)
                .add("imageURL", imageURL)
                .toString();
    }
}