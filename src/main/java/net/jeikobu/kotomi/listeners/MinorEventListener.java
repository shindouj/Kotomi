package net.jeikobu.kotomi.listeners;

import org.pmw.tinylog.Logger;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.ActivityType;
import sx.blah.discord.handle.obj.StatusType;

public class MinorEventListener {

    @EventSubscriber
    public void onReady(ReadyEvent e) {
        Logger.info("Client ready.");
        e.getClient().changePresence(StatusType.DND, ActivityType.PLAYING, "Yosuga no Sora");
    }
}
