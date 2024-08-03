package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.component.BooleanComponent
import dev.aaronhowser.mods.pitchperfect.item.component.ComposerSongComponent
import dev.aaronhowser.mods.pitchperfect.item.component.SoundEventComponent
import dev.aaronhowser.mods.pitchperfect.item.component.UuidComponent
import net.minecraft.core.component.DataComponentType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
        DeferredRegister.createDataComponents(PitchPerfect.ID)

    val SOUND_EVENT_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<SoundEventComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("instrument") {
            it.persistent(SoundEventComponent.CODEC).networkSynchronized(SoundEventComponent.STREAM_CODEC)
        }

    val IS_RECORDING_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<BooleanComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("is_recording") {
            it.persistent(BooleanComponent.CODEC).networkSynchronized(BooleanComponent.STREAM_CODEC)
        }

    val SONG_UUID_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<UuidComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("song_uuid") {
            it.persistent(UuidComponent.CODEC).networkSynchronized(UuidComponent.STREAM_CODEC)
        }

    val COMPOSER_SONG_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<ComposerSongComponent>> =
        DATA_COMPONENT_REGISTRY.registerComponentType("composer_song") {
            it.persistent(ComposerSongComponent.CODEC).networkSynchronized(ComposerSongComponent.STREAM_CODEC)
        }

}