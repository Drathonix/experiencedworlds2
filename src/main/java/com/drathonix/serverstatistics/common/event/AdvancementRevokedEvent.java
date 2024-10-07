package com.drathonix.serverstatistics.common.event;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.server.level.ServerPlayer;

public record AdvancementRevokedEvent(AdvancementHolder advancementHolder, ServerPlayer player) { }
