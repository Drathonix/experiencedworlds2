package com.drathonix.serverstatistics.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;

public class StatChangedEvent  {
    private final int change;
    private boolean canceled = false;
    private final Stat<?> stat;
    private final ServerPlayer player;

    public StatChangedEvent(int change, Stat<?> stat, ServerPlayer player){
        this.change=change;
        this.stat=stat;
        this.player=player;
    }

    public void cancel(){
        canceled =true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public int getChange(){
        return change;
    }
    public Stat<?> getStat(){
        return stat;
    }
    public ServerPlayer getPlayer(){
        return player;
    }
}
