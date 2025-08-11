package dev.aaronhowser.mods.pitchperfect.registry

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.component.ComposerSongComponent
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.*

object ModDataComponents {

	val DATA_COMPONENT_REGISTRY: DeferredRegister.DataComponents =
		DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, PitchPerfect.ID)

	val SOUND_EVENT_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<SoundEvent>> =
		register("instrument", BuiltInRegistries.SOUND_EVENT.byNameCodec(), ByteBufCodecs.registry(Registries.SOUND_EVENT))

	val IS_RECORDING_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<Boolean>> =
		register("is_recording", Codec.BOOL, ByteBufCodecs.BOOL)

	val SONG_UUID_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<UUID>> =
		register("song_uuid", OtherUtil.UUID_CODEC, OtherUtil.UUID_STREAM_CODEC)

	val COMPOSER_SONG_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<ComposerSongComponent>> =
		register("composer_song", ComposerSongComponent.CODEC, ComposerSongComponent.STREAM_CODEC)

	private fun <T> register(
		name: String,
		codec: Codec<T>,
		streamCodec: StreamCodec<in RegistryFriendlyByteBuf, T>
	): DeferredHolder<DataComponentType<*>, DataComponentType<T>> {
		return DATA_COMPONENT_REGISTRY.registerComponentType(name) {
			it.persistent(codec).networkSynchronized(streamCodec)
		}
	}

}