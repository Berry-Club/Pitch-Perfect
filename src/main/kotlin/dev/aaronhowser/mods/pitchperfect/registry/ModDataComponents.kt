package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.component.InstrumentComponent
import dev.aaronhowser.mods.pitchperfect.item.component.MusicItemComponent
import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(PitchPerfect.ID)

    val INSTRUMENT_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<InstrumentComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("instrument") {
            it.persistent(InstrumentComponent.CODEC).networkSynchronized(InstrumentComponent.STREAM_CODEC)
        }

    val MUSIC_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<MusicItemComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("music") {
            it.persistent(MusicItemComponent.CODEC).networkSynchronized(MusicItemComponent.STREAM_CODEC)
        }

}