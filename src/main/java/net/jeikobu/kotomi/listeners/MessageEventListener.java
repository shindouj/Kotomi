package net.jeikobu.kotomi.listeners;

import net.jeikobu.kotomi.malapi.*;
import org.pmw.tinylog.Logger;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.List;
import java.util.Optional;

public class MessageEventListener implements IListener<MessageReceivedEvent> {

    @Override
    public void handle(MessageReceivedEvent messageReceivedEvent) {
        IChannel channel = messageReceivedEvent.getChannel();
        IUser    author  = messageReceivedEvent.getAuthor();
        IMessage message = messageReceivedEvent.getMessage();

        Logger.debug("Received a message! Author: {}, Channel: {}, Message: {}, Type: {}",
                author.getName(), channel.getName(), message.getContent(), message.getType());

        if (message.getType() == IMessage.Type.DEFAULT) {
            String messageContents = message.getContent();
            if (messageContents.startsWith("{") && messageContents.endsWith("}")) {
                Logger.debug("Detected an anime lookup command!");
                String title = messageContents.substring(1, messageContents.length() - 1);

                Optional<AnimeList> optionalList = MALConnector.searchForAnime(title);
                if (optionalList.isPresent()) {
                    List<Anime> animeList = optionalList.get().animeList;
                    Anime selectedAnime = null;

                    for (Anime a: animeList) {
                        if (a.getTitle().equalsIgnoreCase(title)) {
                            selectedAnime = a;
                            break;
                        }
                    }

                    if (selectedAnime == null) selectedAnime = animeList.get(0);

                    Optional<EmbedObject> embed = selectedAnime.toEmbed();
                    if (embed.isPresent()) {
                        channel.sendMessage(embed.get());
                    } else {
                        channel.sendMessage("https://myanimelist.net/anime/" + selectedAnime.getId());
                    }
                } else {
                    channel.sendMessage("Anime not found! Are you sure that's the correct title?");
                }
            }
            if (messageContents.startsWith("<") && messageContents.endsWith(">")) {
                Logger.debug("Detected a manga lookup command!");
                String title = messageContents.substring(1, messageContents.length() - 1);

                Optional<MangaList> optionalList = MALConnector.searchForManga(title);
                if (optionalList.isPresent()) {
                    List<Manga> mangaList = optionalList.get().mangaList;
                    Manga selectedManga = null;

                    for (Manga m: mangaList) {
                        if (m.getTitle().isPresent()) {
                            if (m.getTitle().get().equalsIgnoreCase(title)) {
                                selectedManga = m;
                                break;
                            }
                        }
                    }

                    if (selectedManga == null) selectedManga = mangaList.get(0);

                    Optional<EmbedObject> embed = selectedManga.toEmbed();
                    if (embed.isPresent()) {
                        channel.sendMessage(embed.get());
                    } else {
                        channel.sendMessage("https://myanimelist.net/anime/" + selectedManga.getId());
                    }
                } else {
                    channel.sendMessage("Anime not found! Are you sure that's the correct title?");
                }
            }
        }
    }


}
