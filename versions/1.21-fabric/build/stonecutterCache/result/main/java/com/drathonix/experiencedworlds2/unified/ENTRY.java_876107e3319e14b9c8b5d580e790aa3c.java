package com.vicious.loadmychunks.unified;

import com.drathonix.experiencedworlds2.ExperiencedWorlds;
import com.vicious.loadmychunks.common.LoadMyChunks;


//? if >1.18.2
import com.vicious.loadmychunks.common.util.BoolArgument;


//? if fabric {
import com.vicious.loadmychunks.fabric.LMCFabricInit;
import net.fabricmc.api.ModInitializer;
public class ENTRY implements ModInitializer {
        @Override
        public void onInitialize() {
                LoadMyChunks.init();
                LMCFabricInit.init();
        }
}
//?}

//? elif forge {
/*import com.vicious.loadmychunks.forge.LMCForge;
import net.minecraftforge.fml.common.Mod;

@Mod(LoadMyChunks.MOD_ID)
public class ENTRY {
    public ENTRY() {
        LMCForge.init();
    }
}
*///?}

//? elif neoforge {
/*import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
@Mod(ExperiencedWorlds.MOD_ID)
public class ENTRY {
    public ENTRY(IEventBus meb) {
        ExperiencedWorlds.init();
    }
}
*///?}

