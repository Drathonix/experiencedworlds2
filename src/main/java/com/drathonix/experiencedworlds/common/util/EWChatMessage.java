package com.drathonix.experiencedworlds.common.util;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;

public class EWChatMessage extends BetterChatMessage {
    public EWChatMessage(List<Object> objects) {
        this(objects.toArray());
    }

    private EWChatMessage(Object... objects) {
        super(objects);
        MutableComponent c1 = BetterChatMessage.from(ChatFormatting.RED, ChatFormatting.BOLD, "[", ChatFormatting.RESET, ChatFormatting.GOLD, "EW", ChatFormatting.RED, ChatFormatting.BOLD, "] ", ChatFormatting.RESET).component;
        c1.append(component);
        component = c1;
    }
    
    public static EWChatMessage from(Object... objects){
        return new EWChatMessage(objects);
    }
}
