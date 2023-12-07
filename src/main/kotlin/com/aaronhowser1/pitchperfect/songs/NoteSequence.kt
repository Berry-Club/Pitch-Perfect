package com.aaronhowser1.pitchperfect.songs

import com.aaronhowser1.pitchperfect.event.ModScheduler
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import com.aaronhowser1.pitchperfect.packet.ModPacketHandler
import com.aaronhowser1.pitchperfect.packet.SpawnNotePacket
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.LivingEntity

class NoteSequence(val instrument: InstrumentItem) {

    private val soundResourceLocation: ResourceLocation = instrument.sound.location

    val beats: MutableList<Beat> = mutableListOf()

    data class Beat(val notes: List<Float>, val ticksUntilNextBeat: Int)

    private var currentBeat = 0

    fun play(level: ServerLevel, livingEntity: LivingEntity) {
        playBeat(level, livingEntity)
    }

    private fun playBeat(level: ServerLevel, livingEntity: LivingEntity) {
        val beat = beats.getOrNull(currentBeat) ?: return

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

        currentBeat++

        ModScheduler.scheduleSynchronisedTask(beat.ticksUntilNextBeat) {
            playBeat(level, livingEntity)
        }
    }

}