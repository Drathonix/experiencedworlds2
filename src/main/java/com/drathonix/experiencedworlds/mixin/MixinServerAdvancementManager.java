package com.drathonix.experiencedworlds.mixin;

import com.drathonix.serverstatistics.common.bridge.IMixinServerAdvancementManager;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Map;

@Mixin(ServerAdvancementManager.class)
public class MixinServerAdvancementManager implements IMixinServerAdvancementManager {

    @Shadow private Map<ResourceLocation, AdvancementHolder> advancements;

    @Override
    public @Nullable ResourceLocation getId(Advancement advancement) {
        for (Map.Entry<ResourceLocation, AdvancementHolder> entry : advancements.entrySet()) {
            if(entry.getValue().value().equals(advancement)) {
                return entry.getKey();
            }
        }
        return null;
    }
}
