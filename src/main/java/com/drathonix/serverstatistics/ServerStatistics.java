package com.drathonix.serverstatistics;



import com.drathonix.event.GlobalEvents;
import com.drathonix.experiencedworlds.ExperiencedWorlds;
import com.drathonix.serverstatistics.common.bridge.IMixinDimensionDataStorage;
import com.drathonix.serverstatistics.common.bridge.IMixinPlayerAdvancements;
import com.drathonix.serverstatistics.common.bridge.IMixinStatsCounter;
import com.drathonix.serverstatistics.common.event.AdvancementCompletedEvent;
import com.drathonix.serverstatistics.common.event.AdvancementRevokedEvent;
import com.drathonix.serverstatistics.common.storage.StatData;
import com.mojang.datafixers.util.Pair;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.StatsCounter;

import java.io.File;
import java.util.*;

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
            data.setDirty();
            data.forceSave();
        }
    }

    public static void forceLoginAll() {
        StatData data = getData();
        if(ExperiencedWorlds.server.overworld().getDataStorage() instanceof IMixinDimensionDataStorage mixin){
            File statsDir = mixin.ss$getWorldDir();
            statsDir = new File(statsDir, "stats");
            Set<UUID> newParticipants = new HashSet<>();
            if(statsDir.exists()){
                File[] files = statsDir.listFiles((dir,name)->{
                    UUID id = UUID.fromString(name.replaceAll(".json",""));
                    boolean p = !data.participants.contains(id);
                    if(p){
                        newParticipants.add(id);
                    }
                    return p;
                });
                if(files != null){
                    for (File file : files) {
                        StatsCounter counter = new ServerStatsCounter(ExperiencedWorlds.server,file);
                        if(counter instanceof IMixinStatsCounter mixin2){
                            mixin2.ss$getStats().forEach((k,v)->{
                                data.awardStat(k,v,null);
                            });
                        }
                    }
                }
            }
            File advancementsDir = mixin.ss$getWorldDir();
            advancementsDir = new File(advancementsDir, "advancements");
            File[] files = advancementsDir.listFiles((dir,name)->{
                UUID id = UUID.fromString(name.replaceAll(".json",""));
                boolean p = !data.participants.contains(id);
                if(p){
                    newParticipants.add(id);
                }
                return p;
            });
            if(files != null){
                for (File file : files) {
                    OfflineAdvancements advancements = new OfflineAdvancements(ExperiencedWorlds.server.getFixerUpper(),ExperiencedWorlds.server.getAdvancements(), file.toPath());
                    UUID id = UUID.fromString(file.getName().replaceAll(".json",""));
                    advancements.progress.forEach((a,v)->{
                        if(v.isDone()) {
                            getData().advanceDone(a.value(),id);
                        }
                    });
                }
            }
            data.participants.addAll(newParticipants);
        }
        data.setDirty();
        data.forceSave();
    }

    public static synchronized List<Pair<UUID,StatsCounter>> getPlayerStats(){
        if(ExperiencedWorlds.server.overworld().getDataStorage() instanceof IMixinDimensionDataStorage mixin) {
            File statsDir = mixin.ss$getWorldDir();
            statsDir = new File(statsDir, "stats");
            List<Pair<UUID, StatsCounter>> playerStats = new ArrayList<>();
            if (statsDir.exists()) {
                File[] files = statsDir.listFiles();
                if (files != null) {
                    for (File file : files) {
                        StatsCounter counter = new ServerStatsCounter(ExperiencedWorlds.server, file);
                        playerStats.add(Pair.of(UUID.fromString(file.getName().replaceAll(".json","")), counter));
                    }
                }
            }
            return playerStats;
        }
        throw new IllegalStateException("IMixinDimensionDataStorage mixin did not apply");
    }
}
