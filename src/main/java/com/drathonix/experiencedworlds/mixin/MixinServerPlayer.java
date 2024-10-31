package com.drathonix.experiencedworlds.mixin;

import com.drathonix.serverstatistics.ServerStatistics;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class MixinServerPlayer {
    @Inject(method = "awardStat",at = @At("HEAD"))
    public synchronized void awardGlobal(Stat<?> stat, int value, CallbackInfo ci){
        ServerStatistics.getData().awardStat(stat,value, ss$asSP());
    }

    @Inject(method = "resetStat",at = @At("HEAD"))
    public synchronized void resetGlobal(Stat<?> stat, CallbackInfo ci) {
        ServerStatistics.getData().resetStat(stat, ss$asSP());
    }

    @Unique
    private ServerPlayer ss$asSP(){
        return ServerPlayer.class.cast(this);
    }
}
