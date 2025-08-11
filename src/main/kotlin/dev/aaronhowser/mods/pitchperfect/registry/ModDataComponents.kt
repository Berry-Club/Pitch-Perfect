package dev.aaronhowser.mods.pitchperfect.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.component.ComposerSongComponent
import dev.aaronhowser.mods.pitchperfect.item.component.SoundEventComponent
import dev.aaronhowser.mods.pitchperfect.item.component.UuidComponent
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister

object ModDataComponents {

	val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
		DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PitchPerfect.ID)

	val SOUND_EVENT_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<SoundEventComponent>> =
		register("instrument", SoundEventComponent.CODEC, SoundEventComponent.STREAM_CODEC)

	val IS_RECORDING_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
		register("is_recording", Codec.BOOL, ByteBufCodecs.BOOL)

	val SONG_UUID_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<UuidComponent>> =
		register("song_uuid", UuidComponent.CODEC, UuidComponent.STREAM_CODEC)

	val COMPOSER_SONG_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<ComposerSongComponent>> =
		register("composer_song", ComposerSongComponent.CODEC, ComposerSongComponent.STREAM_CODEC)

	private fun <T> register(
		name: String,
		codec: Codec<T>,
		streamCodec: StreamCodec<out ByteBuf, T>
	): DeferredHolder<DataComponentType<*>, DataComponentType<T>> {
		return DATA_COMPONENT_REGISTRY.registerComponentType(name) {
			it.persistent(codec).networkSynchronized(streamCodec)
		}
	}

}