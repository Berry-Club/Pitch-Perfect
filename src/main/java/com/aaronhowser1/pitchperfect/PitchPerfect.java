package com.aaronhowser1.pitchperfect;

import com.aaronhowser1.pitchperfect.items.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("pitchperfect")
public class PitchPerfect {

    private static final Logger LOGGER = LogManager.getLogger();

    public PitchPerfect() {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_CONFIG);

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

        ModConfig.loadConfig(ModConfig.COMMON_CONFIG, FMLPaths.CONFIGDIR.get().resolve("pitchperfect-common.toml"));

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

    }

    private void setup(final FMLCommonSetupEvent event) {}

    public static final ItemGroup PITCH_PERFECT = new ItemGroup("pitchperfect") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(GuitarItem.ITEMGUITAR);
        }
    };

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new BanjoItem().setRegistryName("banjo"));
            event.getRegistry().register(new BassDrumItem().setRegistryName("bass_drum"));
            event.getRegistry().register(new BassItem().setRegistryName("bass"));
            event.getRegistry().register(new BitItem().setRegistryName("bit"));
            event.getRegistry().register(new ChimesItem().setRegistryName("chimes"));
            event.getRegistry().register(new CowBellItem().setRegistryName("cow_bell"));
            event.getRegistry().register(new DidgeridooItem().setRegistryName("didgeridoo"));
            event.getRegistry().register(new ElectricPianoItem().setRegistryName("electric_piano"));
            event.getRegistry().register(new FluteItem().setRegistryName("flute"));
            event.getRegistry().register(new GlockenspielItem().setRegistryName("glockenspiel"));
            event.getRegistry().register(new GuitarItem().setRegistryName("guitar"));
            event.getRegistry().register(new HarpItem().setRegistryName("harp"));
            event.getRegistry().register(new SnareDrumItem().setRegistryName("snare_drum"));
            event.getRegistry().register(new SticksItem().setRegistryName("sticks"));
            event.getRegistry().register(new VibraphoneItem().setRegistryName("vibraphone"));
            event.getRegistry().register(new XylophoneItem().setRegistryName("xylophone"));
        }
    }
}
