package com.aaronhowser1.pitchperfect;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PitchPerfect.MOD_ID);

    public static final RegistryObject<Item> BANJO = ITEMS.register("banjo", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_BANJO));
    public static final RegistryObject<Item> BASSDRUM = ITEMS.register("bass_drum", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_BASEDRUM));
    public static final RegistryObject<Item> BASS = ITEMS.register("bass", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_BASS));
    public static final RegistryObject<Item> BIT = ITEMS.register("bit", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_BIT));
    public static final RegistryObject<Item> CHIMES = ITEMS.register("chimes", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_CHIME));
    public static final RegistryObject<Item> COW_BELL = ITEMS.register("cow_bell", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_COW_BELL));
    public static final RegistryObject<Item> DIDGERIDOO = ITEMS.register("didgeridoo", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_DIDGERIDOO));
    public static final RegistryObject<Item> PLING = ITEMS.register("electric_piano", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_PLING));
    public static final RegistryObject<Item> FLUTE = ITEMS.register("flute", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_FLUTE));
    public static final RegistryObject<Item> BELL = ITEMS.register("glockenspiel", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_BELL));
    public static final RegistryObject<Item> GUITAR = ITEMS.register("guitar", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_GUITAR));
    public static final RegistryObject<Item> HARP = ITEMS.register("harp", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_HARP));
    public static final RegistryObject<Item> SNARE = ITEMS.register("snare_drum", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_SNARE));
    public static final RegistryObject<Item> HAT = ITEMS.register("sticks", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_HAT));
    public static final RegistryObject<Item> IRON_XYLOPHONE = ITEMS.register("vibraphone", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE));
    public static final RegistryObject<Item> XYLOPHONE = ITEMS.register("xylophone", () -> new InstrumentItem(SoundEvents.NOTE_BLOCK_XYLOPHONE));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
