package com.drathonix.experiencedworlds.mixin;

import com.drathonix.experiencedworlds.ExperiencedWorlds;
import com.drathonix.experiencedworlds.common.data.ExperiencedBorderManager;
import com.drathonix.serverstatistics.common.storage.StatData;
import com.mojang.datafixers.DataFixer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.net.Proxy;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
    @Shadow public abstract ServerLevel overworld();

    @Inject(method = "<init>",at = @At("RETURN"))
    public void capture(Thread thread, LevelStorageSource.LevelStorageAccess levelStorageAccess, PackRepository packRepository, WorldStem worldStem, Proxy proxy, DataFixer dataFixer, Services services, ChunkProgressListenerFactory chunkProgressListenerFactory, CallbackInfo ci){
        ExperiencedWorlds.server=MinecraftServer.class.cast(this);
    }

    @Inject(method = "createLevels",at = @At("RETURN"))
    public void injectCustomSaveData(ChunkProgressListener chunkProgressListener, CallbackInfo ci){
        ExperiencedBorderManager.get(MinecraftServer.class.cast(this));
        ExperiencedBorderManager.growBorder();
        StatData.get(MinecraftServer.class.cast(this));
    }
}
