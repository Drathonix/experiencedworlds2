package com.drathonix.experiencedworlds;

import com.drathonix.experiencedworlds.common.EWCommands;
import com.drathonix.experiencedworlds.common.EWEventHandler;
import com.drathonix.experiencedworlds.common.config.EWCFG;
import com.drathonix.experiencedworlds.common.data.ExperiencedBorderManager;
import com.drathonix.experiencedworlds.mixin.MixinMinecraftServer;
import com.drathonix.serverstatistics.ServerStatistics;
import com.mojang.logging.LogUtils;
import com.vicious.persist.mappify.registry.Stringify;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.networking.NetworkManager;
import net.minecraft.client.gui.screens.options.LanguageSelectScreen;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.StatType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.Executors;

public class ExperiencedWorlds {
    public static final String MOD_ID = "experiencedworlds";
    public static MinecraftServer server;
    private static final Logger LOGGER = LogManager.getLogger("experiencedworlds");

    private static long time = 0;

    public static void init() {
        LOGGER.info("Setting up Experienced Worlds!");
        Stringify.register(ResourceLocation.class,ResourceLocation::tryParse,ResourceLocation::toString);
        //? <1.21.2
        /*Stringify.register(StatType.class,str-> BuiltInRegistries.STAT_TYPE.get(ResourceLocation.tryParse(str)),type->BuiltInRegistries.STAT_TYPE.getKey(type).toString());*/
        //? >1.21.1
        Stringify.register(StatType.class,str-> BuiltInRegistries.STAT_TYPE.get(ResourceLocation.tryParse(str)).get().value(),type->BuiltInRegistries.STAT_TYPE.getKey(type).toString());
        EWCFG.init();
        ServerStatistics.init();
        EWEventHandler.init();
        CommandRegistrationEvent.EVENT.register((dispatcher,registry,selection)->{
            EWCommands.register(dispatcher);
        });
        LOGGER.info("Done setting up Experienced Worlds!");
        TickEvent.SERVER_LEVEL_POST.register(srv->{
            long cur = System.currentTimeMillis();
            if(cur >= time){
                time = cur+EWCFG.autoSaveIntervalMS;
                ExperiencedWorlds.getBorder().forceSave();
                ServerStatistics.getData().forceSave();
            }
        });
    }
    public static ExperiencedBorderManager getBorder(){
        return ExperiencedBorderManager.get(server);
    }
}
