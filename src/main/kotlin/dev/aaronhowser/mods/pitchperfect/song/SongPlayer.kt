package dev.aaronhowser.mods.pitchperfect.song

import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.packet.server_to_client.SpawnNotePacket
import dev.aaronhowser.mods.pitchperfect.util.ModServerScheduler
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.phys.Vec3
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.component1
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.component2
import thedarkcolour.kotlinforforge.neoforge.forge.vectorutil.v3d.component3

class SongPlayer(
    val level: ServerLevel,
    val song: SongSerializer.Song,
    val location: () -> Vec3
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

        println(song)

        for ((instrument, beats) in song.beats) {
            for ((tick, notes) in beats) {
                for (note in notes) {
                    val pitch = note.getGoodPitch()

                    ModServerScheduler.scheduleTaskInTicks(tick) {
                        if (!playing) return@scheduleTaskInTicks

                        val (x, y, z) = location()

                        ModPacketHandler.messageNearbyPlayers(
                            SpawnNotePacket(
                                instrument.soundEvent.value().location,
                                pitch,
                                x,
                                y,
                                z,
                                false
                            ),
                            level,
                            location(),
                            128.0
                        )
                    }
                }
            }
        }
    }
}