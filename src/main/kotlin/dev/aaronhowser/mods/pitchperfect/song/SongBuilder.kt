package dev.aaronhowser.mods.pitchperfect.song

import com.mojang.brigadier.StringReader
import dev.aaronhowser.mods.pitchperfect.song.parts.Beat
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.song.parts.Song.Companion.ID_TO_INSTRUMENT
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument
import java.nio.file.Files
import java.nio.file.Path

object SongBuilder {

	fun fromFile(path: Path): Song? {
		try {
			val string = Files.readString(path)
			val song = fromString(string)

			return song
		} catch (e: Exception) {
			e.printStackTrace()
			return null
		}
	}

}