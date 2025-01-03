package com.drathonix.experiencedworlds.common;

import com.drathonix.experiencedworlds.ExperiencedWorlds;
import com.drathonix.experiencedworlds.common.config.EWCFG;
import com.drathonix.experiencedworlds.common.data.ExperiencedBorderManager;
import com.drathonix.experiencedworlds.common.data.WorldSpecificExperiencedBorder;
import com.drathonix.experiencedworlds.common.util.EWChatMessage;
import com.drathonix.experiencedworlds.common.util.ExpansionPower;
import com.drathonix.serverstatistics.ServerStatistics;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.datafixers.util.Pair;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.storage.ServerLevelData;

import java.util.Optional;
import java.util.UUID;

public class EWCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralArgumentBuilder<CommandSourceStack> cmd = Commands.literal("experiencedworlds")
                .then(Commands.literal("border")
                        .then(Commands.literal("size")
                                .executes(ctx->message(ctx, EWChatMessage.from("<1experiencedworlds.bordersize>", ExperiencedWorlds.getBorder().getTransformedBorderSize()))))
                        .then(Commands.literal("expansions")
                                .executes(ctx->message(ctx,EWChatMessage.from("<1experiencedworlds.expansions>", ExperiencedWorlds.getBorder().getExpansions())))
                        )
                        .then(Commands.literal("multiplier")
                                .executes(ctx->message(ctx,EWChatMessage.from("<1experiencedworlds.multiplier>", ExperiencedWorlds.getBorder().getSizeMultiplier())))
                        )
                        .then(Commands.literal("grow")
                                .requires(ctx->ctx.hasPermission(Commands.LEVEL_ADMINS))
                                .executes(ctx->grow(1))
                                .then(Commands.argument("numgrowths", IntegerArgumentType.integer()).executes(ctx->grow(IntegerArgumentType.getInteger(ctx,"numgrowths")))))
                )
                .then(Commands.literal("config")
                        .requires((ctx)->ctx.hasPermission(Commands.LEVEL_ADMINS) || inIntegratedServer(ctx))
                        .then(Commands.literal("reload")
                                .executes(ctx->{
                                    EWChatMessage.from("<experiencedworlds.reloadedconfig>");
                                    EWCFG.reload();
                                    return 1;
                                })))
                .then(Commands.literal("world")
                        .executes((ctx)->{
                            EWChatMessage.from(((ServerLevelData)getWorld(ctx).getLevelData()).getLevelName()).send(ctx);
                            return 1;
                        })
                        .requires((ctx)->ctx.hasPermission(Commands.LEVEL_ADMINS))
                        .then(Commands.literal("bonusbordermultiplier")
                                .executes(ctx->{
                                    ServerLevel l = getWorld(ctx);
                                    if(l != null) {
                                        EWChatMessage.from("<2experiencedworlds.worldmultiplier>", ((ServerLevelData)l.getLevelData()).getLevelName(), WorldSpecificExperiencedBorder.get(l).multiplier).send(ctx.getSource());
                                    }
                                    else{
                                        EWChatMessage.from("This command is player only").send(ctx.getSource());
                                    }
                                    return 1;
                                })
                                .then(Commands.argument("value",DoubleArgumentType.doubleArg(0))
                                        .executes(ctx->setWorldMultiplier(getWorld(ctx),ctx))))
                        .then(Commands.literal("bonusbordersize")
                                .executes(ctx->{
                                    ServerLevel l = getWorld(ctx);
                                    if(l != null) {
                                        EWChatMessage.from("<2experiencedworlds.worldsize>", ((ServerLevelData)l.getLevelData()).getLevelName(), WorldSpecificExperiencedBorder.get(l).startingSize).send(ctx.getSource());
                                    }
                                    else{
                                        EWChatMessage.from("This command is player only").send(ctx.getSource());
                                    }
                                    return 1;
                                })
                                .then(Commands.argument("value",DoubleArgumentType.doubleArg(0)))
                        )
                )
                .then(Commands.literal("forceloadstats")
                        .requires(ctx->ctx.hasPermission(Commands.LEVEL_ADMINS))
                        .executes(ctx->{
                            EWChatMessage.from("<experiencedworlds.forceloadstatsstart>").send(ctx.getSource());
                            ServerStatistics.forceLoginAll();
                            ExperiencedWorlds.getBorder().recalculate();
                            EWChatMessage.from("<experiencedworlds.forceloadstatsend>").send(ctx.getSource());
                            return 1;
                        })
                )
                .then(Commands.literal("leaderboard")
                        .executes(ctx->{
                            EWChatMessage.from("<experiencedworlds.leaderboardheader>").send(ctx.getSource());
                            int k = 1;
                            for (Pair<UUID, Double> uuidDoublePair : ExpansionPower.getExpansionPowerLeaderBoard()) {
                                if(uuidDoublePair.getFirst() == null) {
                                    EWChatMessage.from(0, ". ", "SERVER", ": ", uuidDoublePair.getSecond().longValue(), "EP").send(ctx.getSource());
                                }
                                else {
                                    Optional<GameProfile> profile = ExperiencedWorlds.server.getProfileCache().get(uuidDoublePair.getFirst());
                                    if (profile.isPresent()) {
                                        EWChatMessage.from(k, ". ", profile.get().getName(), ": ", uuidDoublePair.getSecond().longValue(), "EP").send(ctx.getSource());
                                        k++;
                                        if (k > 10) {
                                            return 1;
                                        }
                                    }
                                }
                            }
                            return 1;
                        })
                ).then(Commands.literal("recalculate")
                        .requires(ctx->ctx.hasPermission(Commands.LEVEL_ADMINS))
                        .executes(ctx->{
                            ExperiencedBorderManager ebm = ExperiencedWorlds.getBorder();
                            EWChatMessage.from("<experiencedworlds.recalcstart>").send(ctx.getSource());
                            ebm.recalculate();
                            EWChatMessage.from("<experiencedworlds.recalcend>").send(ctx.getSource());
                            return 1;
                        })
                );
        dispatcher.register(cmd);
    }

    private static int grow(int amount) {
        ExperiencedBorderManager.grow(amount);
        return 1;
    }

    /**
     * Allows usage on singleplayer and LAN worlds.
     */
    private static boolean inIntegratedServer(CommandSourceStack ctx) {
        return !(ctx.getServer() instanceof DedicatedServer);
    }

    private static int message(CommandContext<CommandSourceStack> ctx, EWChatMessage cm){
        CommandSourceStack stack = ctx.getSource();
        cm.send(stack);
        return 1;
    }
    private static ServerLevel getWorld(CommandContext<CommandSourceStack> ctx){
        if(ctx.getSource().getEntity() instanceof ServerPlayer sp){
            return sp.serverLevel();
        }
        else{
            EWChatMessage.from("This command is player only").send(ctx);
            return null;
        }
    }
    private static int setWorldMultiplier(ServerLevel level, CommandContext<CommandSourceStack> ctx){
        WorldSpecificExperiencedBorder.get(level).multiplier = DoubleArgumentType.getDouble(ctx,"value");
        EWChatMessage.from("<1experiencedworlds.setworldmultiplier>",WorldSpecificExperiencedBorder.get(level).multiplier).send(ctx);
        ExperiencedBorderManager.growBorder();
        return 1;
    }
    private static int setWorldSize(ServerLevel level, CommandContext<CommandSourceStack> ctx){
        WorldSpecificExperiencedBorder.get(level).startingSize = DoubleArgumentType.getDouble(ctx,"value");
        EWChatMessage.from("<1experiencedworlds.setworldsize>",WorldSpecificExperiencedBorder.get(level).startingSize).send(ctx);
        ExperiencedBorderManager.growBorder();
        return 1;
    }
}
