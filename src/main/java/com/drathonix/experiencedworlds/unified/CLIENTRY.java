package com.drathonix.experiencedworlds.unified;


//? if fabric {
/*import net.fabricmc.api.ClientModInitializer;

public class CLIENTRY implements ClientModInitializer {
        @Override
        public void onInitializeClient() {

        }
}
*///?}

//? elif forge {
/*import com.vicious.loadmychunks.forge.LMCForge;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
@Mod.EventBusSubscriber(modid= LoadMyChunks.MOD_ID,bus= Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)
public class LMCCLIENTRY {
        @SubscribeEvent
        public static void clientInit(FMLClientSetupEvent event) {
                LoadMyChunksClient.init();
                LMCForge.clientInit();
        }
}
*///?}

//? elif neoforge {
import com.drathonix.experiencedworlds.ExperiencedWorlds;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
//? if >=1.20.6
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
//? if <1.20.6
/*@Mod.EventBusSubscriber(modid=LoadMyChunks.MOD_ID,bus= Mod.EventBusSubscriber.Bus.MOD,value= Dist.CLIENT)*/
//? if >=1.20.6
@EventBusSubscriber(modid= ExperiencedWorlds.MOD_ID, bus= EventBusSubscriber.Bus.MOD, value= Dist.CLIENT)
public class CLIENTRY {
    @SubscribeEvent
    public static void clientInit(FMLClientSetupEvent event) {
    }
}
//?}

