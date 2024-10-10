//? if neoforge {
package com.drathonix.experiencedworlds.neoforge;

import com.drathonix.experiencedworlds.common.EWEventHandler;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.NeoForgeEventHandler;
import net.neoforged.neoforge.event.server.ServerStartedEvent;

public class NeoForgeInit {
    public static void init(IEventBus meb) {
        NeoForge.EVENT_BUS.register(NeoForgeInit.class);
    }

    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        EWEventHandler.onServerStarted(event.getServer());
    }
}
//?}
