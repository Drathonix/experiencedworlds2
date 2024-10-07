package com.drathonix.experiencedworlds;

import com.drathonix.experiencedworlds.common.EWEventHandler;
import com.drathonix.experiencedworlds.common.config.EWCFG;
import com.drathonix.experiencedworlds.common.data.ExperiencedBorderManager;
import com.drathonix.experiencedworlds.mixin.MixinMinecraftServer;
import com.drathonix.serverstatistics.ServerStatistics;
import com.mojang.logging.LogUtils;
import com.vicious.persist.mappify.registry.Stringify;
import dev.architectury.event.events.common.PlayerEvent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.ServerStatsCounter;
import net.minecraft.stats.StatType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ExperiencedWorlds {
    public static final String modid = "experiencedworlds";
    public static MinecraftServer server;
    private static final Logger LOGGER = LogManager.getLogger("experiencedworlds");

    public static void init() {
        LOGGER.info("Setting up Experienced Worlds!");
        Stringify.register(ResourceLocation.class,ResourceLocation::tryParse,ResourceLocation::toString);
        Stringify.register(StatType.class,str-> BuiltInRegistries.STAT_TYPE.get(ResourceLocation.tryParse(str)),type->BuiltInRegistries.STAT_TYPE.getKey(type).toString());
        EWCFG.init();
        ServerStatistics.init();
        EWEventHandler.init();
        LOGGER.info("Done setting up Experienced Worlds!");
    }
    public static ExperiencedBorderManager getBorder(){
        return ExperiencedBorderManager.get(server);
    }
}
