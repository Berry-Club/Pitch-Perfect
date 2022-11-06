package com.aaronhowser1.pitchperfect;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ObjectHolder;
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
            return new ItemStack(BASS);
        }
    };

    @ObjectHolder("pitchperfect:bass")
    public static Item BASS;

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_BANJO).setRegistryName("banjo"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_BASEDRUM).setRegistryName("bass_drum"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_BASS).setRegistryName("bass"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_BIT).setRegistryName("bit"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_CHIME).setRegistryName("chimes"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_COW_BELL).setRegistryName("cow_bell"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_DIDGERIDOO).setRegistryName("didgeridoo"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_PLING).setRegistryName("electric_piano"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_FLUTE).setRegistryName("flute"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_BELL).setRegistryName("glockenspiel"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_GUITAR).setRegistryName("guitar"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_HARP).setRegistryName("harp"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_SNARE).setRegistryName("snare_drum"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_HAT).setRegistryName("sticks"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE).setRegistryName("vibraphone"));
            event.getRegistry().register(new InstrumentItem(() -> SoundEvents.BLOCK_NOTE_BLOCK_XYLOPHONE).setRegistryName("xylophone"));
        }
    }
}
