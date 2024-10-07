package com.drathonix.experiencedworlds.mixin;

import com.drathonix.event.GlobalEvents;
import com.drathonix.serverstatistics.common.bridge.IMixinPlayerAdvancements;
import com.drathonix.serverstatistics.common.event.AdvancementCompletedEvent;
import com.drathonix.serverstatistics.common.event.AdvancementRevokedEvent;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(PlayerAdvancements.class)
public abstract class MixinPlayerAdvancements implements IMixinPlayerAdvancements {
    @Shadow public abstract AdvancementProgress getOrStartProgress(AdvancementHolder arg);

    @Shadow private ServerPlayer player;

    @Shadow @Final private Map<AdvancementHolder, AdvancementProgress> progress;

    @Inject(method = "award",at = @At("RETURN"))
    public void interceptDone(AdvancementHolder advancementHolder, String string, CallbackInfoReturnable<Boolean> cir){
        AdvancementProgress advancementProgress = this.getOrStartProgress(advancementHolder);
        if(advancementProgress.isDone()){
            GlobalEvents.post(new AdvancementCompletedEvent(advancementHolder,player));
        }
    }

    @Inject(method = "revoke",at = @At("RETURN"))
    public void interceptRevoked(AdvancementHolder advancementHolder, String string, CallbackInfoReturnable<Boolean> cir){
        AdvancementProgress advancementProgress = this.getOrStartProgress(advancementHolder);
        //Will not run if empty.
        for (String completedCriterion : advancementProgress.getCompletedCriteria()) {
            return;
        }
        GlobalEvents.post(new AdvancementRevokedEvent(advancementHolder,player));
    }

    @Override
    public Map<AdvancementHolder, AdvancementProgress> getProgress() {
        return progress;
    }
}
