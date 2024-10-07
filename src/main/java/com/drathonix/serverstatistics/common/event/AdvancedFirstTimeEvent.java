package com.drathonix.serverstatistics.common.event;

import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;

public class AdvancedFirstTimeEvent  {
    private final ServerPlayer player;
    private final Advancement advancementKey;
    public AdvancedFirstTimeEvent(ServerPlayer sp, Advancement key) {
        this.player=sp;
        this.advancementKey=key;
    }
    public ServerPlayer getPlayer(){
        return player;
    }
    public Advancement getAdvancement(){
        return advancementKey;
    }
}
