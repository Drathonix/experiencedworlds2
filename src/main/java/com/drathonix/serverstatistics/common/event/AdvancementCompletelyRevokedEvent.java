package com.drathonix.serverstatistics.common.event;

import net.minecraft.advancements.Advancement;
import net.minecraft.server.level.ServerPlayer;

public class AdvancementCompletelyRevokedEvent {
    private final ServerPlayer player;
    private final Advancement advancementKey;
    public AdvancementCompletelyRevokedEvent(ServerPlayer sp, Advancement key) {
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
