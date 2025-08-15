package dev.aaronhowser.mods.pitchperfect.screen.composer.parts

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.item.InstrumentItem
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import io.netty.buffer.ByteBuf
import net.minecraft.core.Holder
import net.minecraft.network.chat.Component
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import net.neoforged.neoforge.registries.DeferredItem

enum class ScreenInstrument(
	val image: ResourceLocation,
	val noteBlockInstrument: NoteBlockInstrument,
	val deferredItem: DeferredItem<InstrumentItem>
) {
	BANJO(
		ScreenTextures.Sprite.Instrument.BANJO,
		NoteBlockInstrument.BANJO,
		ModItems.BANJO
	),
	BASS(
		ScreenTextures.Sprite.Instrument.BASS,
		NoteBlockInstrument.BASS,
		ModItems.BASS
	),
	BASS_DRUM(
		ScreenTextures.Sprite.Instrument.BASS_DRUM,
		NoteBlockInstrument.BASEDRUM,
		ModItems.BASS_DRUM
	),
	BIT(
		ScreenTextures.Sprite.Instrument.BIT,
		NoteBlockInstrument.BIT,
		ModItems.BIT
	),
	CHIMES(
		ScreenTextures.Sprite.Instrument.CHIMES,
		NoteBlockInstrument.CHIME,
		ModItems.CHIMES
	),
	COW_BELL(
		ScreenTextures.Sprite.Instrument.COW_BELL,
		NoteBlockInstrument.COW_BELL,
		ModItems.COW_BELL
	),
	DIDGERIDOO(
		ScreenTextures.Sprite.Instrument.DIDGERIDOO,
		NoteBlockInstrument.DIDGERIDOO,
		ModItems.DIDGERIDOO
	),
	ELECTRIC_PIANO(
		ScreenTextures.Sprite.Instrument.ELECTRIC_PIANO,
		NoteBlockInstrument.PLING,
		ModItems.ELECTRIC_PIANO
	),
	FLUTE(
		ScreenTextures.Sprite.Instrument.FLUTE,
		NoteBlockInstrument.FLUTE,
		ModItems.FLUTE
	),
	GLOCKENSPIEL(
		ScreenTextures.Sprite.Instrument.GLOCKENSPIEL,
		NoteBlockInstrument.BELL,
		ModItems.GLOCKENSPIEL
	),
	GUITAR(
		ScreenTextures.Sprite.Instrument.GUITAR,
		NoteBlockInstrument.GUITAR,
		ModItems.GUITAR
	),
	HARP(
		ScreenTextures.Sprite.Instrument.HARP,
		NoteBlockInstrument.HARP,
		ModItems.HARP
	),
	SNARE_DRUM(
		ScreenTextures.Sprite.Instrument.SNARE_DRUM,
		NoteBlockInstrument.SNARE,
		ModItems.SNARE_DRUM
	),
	STICKS(
		ScreenTextures.Sprite.Instrument.STICKS,
		NoteBlockInstrument.HAT,
		ModItems.STICKS
	),
	VIBRAPHONE(
		ScreenTextures.Sprite.Instrument.VIBRAPHONE,
		NoteBlockInstrument.IRON_XYLOPHONE,
		ModItems.VIBRAPHONE
	),
	XYLOPHONE(
		ScreenTextures.Sprite.Instrument.XYLOPHONE,
		NoteBlockInstrument.XYLOPHONE,
		ModItems.XYLOPHONE
	);

	val displayName: Component by lazy {
		deferredItem.get().defaultInstance.hoverName
	}

	companion object {

		val CODEC: Codec<ScreenInstrument> =
			Codec.STRING.xmap(::valueOf, ScreenInstrument::name)

		val STREAM_CODEC: StreamCodec<ByteBuf, ScreenInstrument> =
			ByteBufCodecs.STRING_UTF8.map(::valueOf, ScreenInstrument::name)

		fun fromSound(soundHolder: Holder<SoundEvent>): ScreenInstrument? {
			return entries.firstOrNull { it.noteBlockInstrument.soundEvent == soundHolder }
		}

	}
}