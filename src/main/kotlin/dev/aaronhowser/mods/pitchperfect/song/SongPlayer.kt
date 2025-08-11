package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SpawnNotePacket
import dev.aaronhowser.mods.pitchperfect.song.parts.Song
import dev.aaronhowser.mods.pitchperfect.util.ModServerScheduler
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.component1
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.component2
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.component3
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3
import java.util.function.Supplier

class SongPlayer(
	val level: ServerLevel,
	val song: Song,
	val location: Supplier<Vec3>
) {

	private var playing: Boolean = false
	private var started: Boolean = false

	fun stopPlaying() {
		playing = false
	}

	fun startPlaying() {
		if (started) return
		playing = true
		started = true

		for ((instrument, beats) in song.beats) {

			for ((tick, notes) in beats) {
				for (note in notes) {
					val pitch = note.getGoodPitch()

					ModServerScheduler.scheduleTaskInTicks(tick) {
						if (!playing) return@scheduleTaskInTicks

						val (x, y, z) = location.get()

						ModPacketHandler.messageNearbyPlayers(
							SpawnNotePacket(
								instrument.value().location,
								pitch,
								x,
								y,
								z,
								false
							),
							level,
							location.get(),
							128.0
						)
					}
				}
			}
		}
	}
}