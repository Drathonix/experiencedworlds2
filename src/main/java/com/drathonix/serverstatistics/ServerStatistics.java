package com.drathonix.serverstatistics;



import com.drathonix.event.GlobalEvents;
import com.drathonix.experiencedworlds.ExperiencedWorlds;
import com.drathonix.serverstatistics.common.bridge.IMixinPlayerAdvancements;
import com.drathonix.serverstatistics.common.bridge.IMixinStatsCounter;
import com.drathonix.serverstatistics.common.event.AdvancementCompletedEvent;
import com.drathonix.serverstatistics.common.event.AdvancementRevokedEvent;
import com.drathonix.serverstatistics.common.storage.StatData;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

public class ServerStatistics {
    public static void init() {
        PlayerEvent.PLAYER_JOIN.register(ServerStatistics::onLogin);
        GlobalEvents.addListener(AdvancementCompletedEvent.class,ServerStatistics::onComplete);
        GlobalEvents.addListener(AdvancementRevokedEvent.class,ServerStatistics::onRevoke);
    }

    public static StatData getData(){
        return StatData.get(ExperiencedWorlds.server);
    }

    public static void onComplete(AdvancementCompletedEvent event){
        getData().advanceDone(event.advancementHolder().value(), event.player());
    }

    public static void onRevoke(AdvancementRevokedEvent event){
        getData().advanceRevoked(event.advancementHolder().value(), event.player());
    }

    public static void onLogin(ServerPlayer sp){
        StatData data = getData();
        if(!getData().participants.contains(sp.getUUID())){
            Map<AdvancementHolder, AdvancementProgress> advancements = ((IMixinPlayerAdvancements)sp.getAdvancements()).getProgress();
            advancements.forEach((a,v)->{
                if(v.isDone()) {
                    getData().advanceDone(a.value(),sp);
                }
            });
            if(sp.getStats() instanceof IMixinStatsCounter mixin){
                mixin.ss$getStats().forEach((k,v)->{
                  data.awardStat(k,v,sp);
                });
            }
            data.participants.add(sp.getUUID());
        }
    }
}
