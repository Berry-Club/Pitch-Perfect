package com.aaronhowser1.pitchperfect.utils

import com.aaronhowser1.pitchperfect.event.ModScheduler
import com.aaronhowser1.pitchperfect.item.InstrumentItem
import net.minecraft.core.BlockPos
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.Level

class NoteSequence(val instrument: InstrumentItem) {

    private val beats: List<Beat> = listOf()

    data class Beat(val notes: List<Float>, val ticksUntilNextBeat: Int)

    private var currentBeat = 0

    fun play(level: Level, blockPos: BlockPos) {
        if (!level.isClientSide) return

        playBeat(level, blockPos)
    }

    private fun playBeat(level: Level, blockPos: BlockPos) {
        val beat = beats.getOrNull(currentBeat) ?: return

        for (pitch in beat.notes) {
            level.playSound(
                null,
                blockPos,
                instrument.sound,
                SoundSource.HOSTILE,
                1f,
                pitch
            )
        }

        currentBeat++

        ModScheduler.scheduleSynchronisedTask(beat.ticksUntilNextBeat) {
            playBeat(level, blockPos)
        }
    }

}