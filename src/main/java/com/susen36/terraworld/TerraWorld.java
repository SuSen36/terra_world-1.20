package com.susen36.terraworld;

import com.susen36.terraworld.init.BlocksInit;
import com.susen36.terraworld.init.ItemsInit;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TerraWorld.MODID)
public class TerraWorld {

    public static final String MODID = "terra_world";

    public static final Logger LOGGER = LogManager.getLogger(MODID);



    public static ResourceKey<Level> TERRA_DIMENSION;



    public TerraWorld() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);

        ItemsInit.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BlocksInit.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        TERRA_DIMENSION = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(TerraWorld.MODID, "terra"));
    }


}