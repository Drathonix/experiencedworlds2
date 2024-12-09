//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.drathonix.serverstatistics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import net.minecraft.FileUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementNode;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.advancements.AdvancementTree;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.CriterionProgress;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.protocol.game.ClientboundSelectAdvancementsTabPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateAdvancementsPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.advancements.AdvancementVisibilityEvaluator;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.datafix.DataFixTypes;
import net.minecraft.world.level.GameRules;
import org.slf4j.Logger;

public class OfflineAdvancements {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Path playerSavePath;
    public final Map<AdvancementHolder, AdvancementProgress> progress = new LinkedHashMap();
    private final Codec<Data> codec;

    public OfflineAdvancements(DataFixer dataFixer, ServerAdvancementManager arg2, Path path) {
        this.playerSavePath = path;
        this.codec = DataFixTypes.ADVANCEMENTS.wrapCodec(OfflineAdvancements.Data.CODEC, dataFixer, 1343);
        this.load(arg2);
    }

    private void load(ServerAdvancementManager arg) {
        if (Files.isRegularFile(this.playerSavePath, new LinkOption[0])) {
            try {
                JsonReader jsonreader = new JsonReader(Files.newBufferedReader(this.playerSavePath, StandardCharsets.UTF_8));

                try {
                    jsonreader.setLenient(false);
                    JsonElement jsonelement = Streams.parse(jsonreader);
                    Data playeradvancements$data = (Data)this.codec.parse(JsonOps.INSTANCE, jsonelement).getOrThrow(JsonParseException::new);
                    this.applyFrom(arg, playeradvancements$data);
                } catch (Throwable var6) {
                    try {
                        jsonreader.close();
                    } catch (Throwable var5) {
                        var6.addSuppressed(var5);
                    }

                    throw var6;
                }

                jsonreader.close();
            } catch (IOException | JsonIOException var7) {
                Exception ioexception = var7;
                LOGGER.error("Couldn't access player advancements in {}", this.playerSavePath, ioexception);
            } catch (JsonParseException var8) {
                JsonParseException jsonparseexception = var8;
                LOGGER.error("Couldn't parse player advancements in {}", this.playerSavePath, jsonparseexception);
            }
        }
    }


    private void applyFrom(ServerAdvancementManager arg, Data arg2) {
        arg2.forEach((arg2x, arg3) -> {
            AdvancementHolder advancementholder = arg.get(arg2x);
            if (advancementholder == null) {
                LOGGER.warn("Ignored advancement '{}' in progress file {} - it doesn't exist anymore?", arg2x, this.playerSavePath);
            } else {
                this.startProgress(advancementholder, arg3);
            }

        });
    }

    private void startProgress(AdvancementHolder arg, AdvancementProgress arg2) {
        arg2.update(arg.value().requirements());
        this.progress.put(arg, arg2);
    }

    static record Data(Map<ResourceLocation, AdvancementProgress> map) {
        public static final Codec<Data> CODEC;

        Data(Map<ResourceLocation, AdvancementProgress> map) {
            this.map = map;
        }

        public void forEach(BiConsumer<ResourceLocation, AdvancementProgress> biConsumer) {
            this.map.entrySet().stream().sorted(Entry.comparingByValue()).forEach((entry) -> {
                biConsumer.accept((ResourceLocation)entry.getKey(), (AdvancementProgress)entry.getValue());
            });
        }

        public Map<ResourceLocation, AdvancementProgress> map() {
            return this.map;
        }

        static {
            CODEC = Codec.unboundedMap(ResourceLocation.CODEC, AdvancementProgress.CODEC).xmap(Data::new, Data::map);
        }
    }
}
