package com.drathonix.experiencedworlds.common.util;

import com.drathonix.experiencedworlds.common.config.EWCFG;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.Entity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This class was mainly created to make coding chat messages easier. Here's what you need to know.
 * ChatFormatting is applied to all stylable components after it.
 * A 'Styable Component' is any Object that isn't chat formatting. This includes Component instances as well as anything with a working toString function (should be everything I hope).
 * Translatable text can be provided by providing a string surrounded with angle brackets such as: "<mod.translation>"
 * If the translation has insertions such as this: "Hi %1$s, I'm Drathon!" The amount of insertion places must be indicated with a number
 * after the left angle bracket like so "<1viciouscore.devgreeting>". Any inserted stylable components must then come after the translation code wise.
 * I.E.: BetterChatMessage.from(ChatFormatting.GOLD,"<1viciouscore.devgreeting>", ChatFormatting.BLUE,playerName);
 *
 * This is far superior to calling multiple methods or writing JSON blocks just to send simple sentences.
 * With more complex messages such as ones using actions the mojang provided system is actually very practical and is required with BCM.
 */
public class BetterChatMessage {
    public MutableComponent component;

    public BetterChatMessage(List<Object> objects){
        this(objects.toArray());
    }
    public BetterChatMessage(Object... objects){
        component = Component.empty();
        Component unstyled = null;
        List<ChatFormatting> formatting = new ArrayList<>();
        for (int i = 0; i < objects.length; i++) {
            Object object = objects[i];
            if(object == null) object = "null";
            if(object instanceof String str){
                if(str.startsWith("<") && str.endsWith(">")) {
                    if(Character.isDigit(str.charAt(1))){
                        StringBuilder val = new StringBuilder();
                        for (int j = 1; j < str.length(); j++) {
                            if(!Character.isDigit(str.charAt(j))){
                                break;
                            }
                            else{
                                val.append(str.charAt(j));
                            }
                        }
                        Tuple<Object[],Integer> ret = gatherComponentsForTranslation(objects,i+1, Integer.parseInt(val.toString()));
                        String key = str.substring(1+val.toString().length(),str.length()-1);
                        unstyled = Component.translatableWithFallback(key, Language.getInstance().getOrDefault(key), ret.getA());
                        i = (int) ret.getB();
                    }
                    else {
                        String key = str.substring(1, str.length() - 1);
                        unstyled = Component.translatableWithFallback(key,Language.getInstance().getOrDefault(key));
                    }
                }
                else{
                    unstyled = Component.literal(str);
                }
            }
            else if(object instanceof ChatFormatting cf){
                if(cf != ChatFormatting.RESET){
                    formatting.add(cf);
                }
                else{
                    formatting.clear();
                }
            }
            else if(object instanceof Component cp){
                unstyled = cp;
            }
            else{
                unstyled = Component.literal(object.toString());
            }
            if(unstyled != null) {
                if(unstyled instanceof MutableComponent stylable) {
                    for (ChatFormatting chatFormatting : formatting) {
                        stylable.withStyle(chatFormatting);
                    }
                }
                component.append(unstyled);
                unstyled = null;
            }
        }
    }
    public void send(Entity... e){
        for (Entity entity : e) {
            entity.sendSystemMessage(component);
        }
    }
    public void send(Collection<? extends Entity> ents){
        for (Entity ent : ents) {
            ent.sendSystemMessage(component);
        }
    }
    public void sendExcept(Entity except, Collection<? extends Entity> ents){
        for (Entity ent : ents) {
            if(ent != except){
                ent.sendSystemMessage(component);
            }
        }
    }
    public void send(CommandSourceStack sender){
        sender.sendSystemMessage(component);
    }

    public static BetterChatMessage from(Object... objects){
        return new BetterChatMessage(objects);
    }
    protected Tuple<Object[],Integer> gatherComponentsForTranslation(Object[] objs, int start, int expected){
        Component[] compsOut = new Component[expected];
        int j = start;
        List<Object> comps = new ArrayList<>();
        for (int i = 0; i < expected; i++) {
            while (j < objs.length) {
                Object o = objs[j];
                j++;
                comps.add(o);
                //TODO: Allow nested translation keys.
                if (!(o instanceof ChatFormatting)) {
                    compsOut[i] = new BetterChatMessage(comps).component;
                    comps.clear();
                    break;
                }
            }
        }
        return new Tuple<>(compsOut,j);
    }

    public void send(CommandContext<CommandSourceStack> ctx) {
        send(ctx.getSource());
    }
}