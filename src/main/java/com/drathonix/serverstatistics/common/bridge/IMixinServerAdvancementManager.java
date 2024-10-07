package com.drathonix.serverstatistics.common.bridge;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IMixinServerAdvancementManager {
    ResourceLocation getId(Advancement advancement);

    static @Nullable ResourceLocation getId(@NotNull MinecraftServer server, Advancement advancement) {
        if(server.getAdvancements() instanceof IMixinServerAdvancementManager mixin){
            return mixin.getId(advancement);
        }
        return null;
    }
}
