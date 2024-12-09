package com.drathonix.serverstatistics.common.event;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import org.jetbrains.annotations.Nullable;

public class StatChangedEvent  {
    private final int change;
    private boolean canceled = false;
    private final Stat<?> stat;
    private final ServerPlayer player;

    public StatChangedEvent(int change, Stat<?> stat, @Nullable ServerPlayer player){
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
    public @Nullable ServerPlayer getPlayer(){
        return player;
    }
}
