package com.aaronhowser1.pitchperfect.song

import com.aaronhowser1.pitchperfect.event.ModScheduler
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.packet.ModPacketHandler
import com.aaronhowser1.pitchperfect.packet.SpawnNotePacket
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity

class NoteSequence(
    val name: String,
    val instrument: InstrumentItem,
    val beats: MutableList<Beat> = mutableListOf()
) {

    private val soundResourceLocation: ResourceLocation = instrument.sound.location

    data class Beat(val notes: List<Float>, val ticksUntilNextBeat: Int)

    fun toggle(level: ServerLevel, livingEntity: LivingEntity) {

        when (val currentSong = SongRegistry.songsPlaying[livingEntity]) {
            null -> {
                start(level, livingEntity)
            }

            this -> {
                stopPlaying(livingEntity)
            }

            else -> {
                currentSong.stopPlaying(livingEntity)
                start(level, livingEntity)
            }
        }
    }

    private fun start(level: ServerLevel, livingEntity: LivingEntity) {
        SongRegistry.songsPlaying[livingEntity] = this
        playBeat(level, livingEntity, 0)
    }

    private fun stopPlaying(livingEntity: LivingEntity) {
        SongRegistry.songsPlaying.remove(livingEntity)
    }

    private fun playBeat(level: ServerLevel, livingEntity: LivingEntity, iteration: Int) {

        if (SongRegistry.songsPlaying[livingEntity] != this) return

        val beat = beats.getOrNull(iteration)

        if (beat == null) {
            stopPlaying(livingEntity)
            return
        }

        val eyePos = livingEntity.eyePosition

        for (pitch in beat.notes) {

            ModPacketHandler.messageNearbyPlayers(
                SpawnNotePacket(
                    soundResourceLocation,
                    pitch,
                    eyePos.x,
                    eyePos.y + 1.5,
                    eyePos.z
                ),
                level,
                eyePos,
                128.0
            )
        }


        ModScheduler.scheduleSynchronisedTask(beat.ticksUntilNextBeat) {
            playBeat(level, livingEntity, iteration + 1)
        }
    }

}