package com.drathonix.experiencedworlds.common;

import com.drathonix.event.GlobalEvents;
import com.drathonix.experiencedworlds.ExperiencedWorlds;
import com.drathonix.experiencedworlds.common.config.EWCFG;
import com.drathonix.experiencedworlds.common.data.ExperiencedBorderManager;
import com.drathonix.experiencedworlds.common.data.WorldSpecificExperiencedBorder;
import com.drathonix.experiencedworlds.common.math.EWMath;
import com.drathonix.experiencedworlds.common.util.EWChatMessage;
import com.drathonix.serverstatistics.ServerStatistics;
import com.drathonix.serverstatistics.common.event.AdvancedFirstTimeEvent;
import com.drathonix.serverstatistics.common.event.ServerStatsResetEvent;
import com.drathonix.serverstatistics.common.event.StatChangedEvent;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.EntityEvent;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.StatsCounter;
import net.minecraft.world.level.GameType;


import java.util.HashSet;
import java.util.Set;

public class EWEventHandler {
    public static void init(){
        GlobalEvents.addListener(AdvancedFirstTimeEvent.class,EWEventHandler::increaseMultiplier);
        GlobalEvents.addListener(StatChangedEvent.class,EWEventHandler::onStatChanged);
        EntityEvent.LIVING_HURT.register(((entity, source, amount) -> {
            if(ExperiencedBorderManager.difficulty != null){
                return EventResult.interruptFalse();
            }
            return EventResult.pass();
        }));
        PlayerEvent.PLAYER_JOIN.register(ExperiencedBorderManager::onJoin);
    }


    public synchronized static void onStatChanged(StatChangedEvent sce){
        Stat<?> stat = sce.getStat();
        StatType<?> type = stat.getType();
        if(EWCFG.gameplay.activeStats.contains(type)){
            StatsCounter counter = ServerStatistics.getData().counter;
            int current = counter.getValue(stat);
            if (current > 0) {
                int borderChange = EWCFG.gameplay.logarithmicStatRequirement ? (int) EWMath.logConfigBase(current + sce.getChange()) - (int)EWMath.logConfigBase(current) : current+sce.getChange() - current;
                if (borderChange != 0) {
                    ExperiencedBorderManager.increaseBorder(borderChange, sce);
                }
            } else if(EWCFG.gameplay.awardOne) {
                ExperiencedBorderManager.increaseBorder(1, sce);
            }
        }
    }

    public synchronized static void onServerStarted(MinecraftServer server){
        for (ServerLevel l : server.getAllLevels()) {
            WorldSpecificExperiencedBorder dat = WorldSpecificExperiencedBorder.get(l);
            double newSize = ExperiencedWorlds.getBorder().getTransformedBorderSize() * Math.max(1, dat.multiplier) + dat.startingSize;
            l.getWorldBorder().setSize(newSize);
        }
    }

    public synchronized static void increaseMultiplier(AdvancedFirstTimeEvent afte){
        ExperiencedBorderManager swb = ExperiencedBorderManager.get(ExperiencedWorlds.server);
        boolean announce = EWCFG.sendAdvancementAnnouncements() && !swb.maximumMultiplier();
        double a2 = Math.round(swb.getCurrentMultiplierGain()*100.0)/100.0;
        if(announce && afte.getPlayer() != null){
            EWChatMessage.from("<3experiencedworlds.advancementattained>",afte.getPlayer().getDisplayName(),a2,Math.round(swb.getSizeMultiplier()*100.0)/100.0).send(ExperiencedWorlds.server.getPlayerList().getPlayers());
        }
        ExperiencedBorderManager.growBorder();
    }
}