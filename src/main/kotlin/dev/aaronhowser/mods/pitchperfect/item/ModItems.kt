package dev.aaronhowser.mods.pitchperfect.item

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import net.minecraft.sounds.SoundEvent
import net.minecraft.sounds.SoundEvents
import net.minecraft.world.item.Item
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries

@Suppress("unused", "HasPlatformType")
object ModItems {

    val ITEM_REGISTRY: DeferredRegister<Item> =
        DeferredRegister.create(ForgeRegistries.ITEMS, PitchPerfect.MOD_ID)

    private fun createInstrumentItem(name: String, sound: SoundEvent) =
        ITEM_REGISTRY.register<Item>(name) { InstrumentItem(sound) }

    val BANJO = createInstrumentItem("banjo", SoundEvents.NOTE_BLOCK_BANJO)
    val BASS_DRUM = createInstrumentItem("bass_drum", SoundEvents.NOTE_BLOCK_BASEDRUM)
    val BASS = createInstrumentItem("bass", SoundEvents.NOTE_BLOCK_BASS)
    val BIT = createInstrumentItem("bit", SoundEvents.NOTE_BLOCK_BIT)
    val CHIMES = createInstrumentItem("chimes", SoundEvents.NOTE_BLOCK_CHIME)
    val COW_BELL = createInstrumentItem("cow_bell", SoundEvents.NOTE_BLOCK_COW_BELL)
    val DIDGERIDOO = createInstrumentItem("didgeridoo", SoundEvents.NOTE_BLOCK_DIDGERIDOO)
    val PLING = createInstrumentItem("electric_piano", SoundEvents.NOTE_BLOCK_PLING)
    val FLUTE = createInstrumentItem("flute", SoundEvents.NOTE_BLOCK_FLUTE)
    val BELL = createInstrumentItem("glockenspiel", SoundEvents.NOTE_BLOCK_BELL)
    val GUITAR = createInstrumentItem("guitar", SoundEvents.NOTE_BLOCK_GUITAR)
    val HARP = createInstrumentItem("harp", SoundEvents.NOTE_BLOCK_HARP)
    val SNARE = createInstrumentItem("snare_drum", SoundEvents.NOTE_BLOCK_SNARE)
    val HAT = createInstrumentItem("sticks", SoundEvents.NOTE_BLOCK_HAT)
    val IRON_XYLOPHONE = createInstrumentItem("vibraphone", SoundEvents.NOTE_BLOCK_IRON_XYLOPHONE)
    val XYLOPHONE = createInstrumentItem("xylophone", SoundEvents.NOTE_BLOCK_XYLOPHONE)

    fun register(eventBus: IEventBus) {
        ITEM_REGISTRY.register(eventBus)
    }

}