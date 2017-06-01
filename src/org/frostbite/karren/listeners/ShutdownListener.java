package org.frostbite.karren.listeners;

import org.knowm.yank.Yank;
import sx.blah.discord.api.events.IListener;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;

/**
 * Created by ddakebono on 5/31/2017.
 */
public class ShutdownListener implements IListener<DisconnectedEvent> {
    @Override
    public void handle(DisconnectedEvent disconnectedEvent) {
        //Lets release the Yank SQL connection pool
        Yank.releaseAllConnectionPools();
    }
}
