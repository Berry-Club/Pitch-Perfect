package dev.aaronhowser.mods.pitchperfect.block.entity.composer

import net.minecraft.core.Holder
import net.minecraft.sounds.SoundEvent

class ComposerTimeline {

    private data class DelayPitch(
        val delay: Int,
        val pitch: Int
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is DelayPitch) return false
            return delay == other.delay && pitch == other.pitch
        }

        override fun hashCode(): Int {
            var result = delay
            result = 31 * result + pitch
            return result
        }
    }

    private val soundCounts: MutableMap<Holder<SoundEvent>, MutableList<DelayPitch>> = mutableMapOf()

    fun addSoundAt(
        delay: Int,
        pitch: Int,
        sound: Holder<SoundEvent>
    ) {
        val delayPitch = DelayPitch(delay, pitch)
        val soundList = soundCounts.getOrDefault(sound, mutableListOf())
        soundList.add(delayPitch)
        soundCounts[sound] = soundList

        println(soundCounts)
    }

    fun removeSoundAt(
        delay: Int,
        pitch: Int,
        sound: Holder<SoundEvent>
    ) {
        val delayPitch = DelayPitch(delay, pitch)
        val soundList = soundCounts.getOrDefault(sound, mutableListOf())
        soundList.remove(delayPitch)
        soundCounts[sound] = soundList

        println(soundCounts)
    }

    fun getSoundsAt(
        delay: Int,
        pitch: Int
    ): List<Holder<SoundEvent>> {
        val delayPitch = DelayPitch(delay, pitch)

        return soundCounts
            .filter { delayPitch in it.value }
            .keys
            .toList()
    }

    companion object {

        private const val DELAY = "delay"
        private const val PITCH = "pitch"
        private const val SOUND_RL = "sound_rl"
        private const val SOUND_COUNT = "sound_count"


    }

}