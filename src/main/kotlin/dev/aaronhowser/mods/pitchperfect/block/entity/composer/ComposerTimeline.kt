package dev.aaronhowser.mods.pitchperfect.block.entity.composer

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag

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

    private val soundCounts: MutableMap<String, MutableList<DelayPitch>> = mutableMapOf()

    fun addSoundAt(
        delay: Int,
        pitch: Int,
        soundName: String
    ) {
        val delayPitch = DelayPitch(delay, pitch)
        val soundList = soundCounts.getOrDefault(soundName, mutableListOf())
        soundList.add(delayPitch)
        soundCounts[soundName] = soundList

        println(soundCounts)
    }

    fun removeSoundAt(
        delay: Int,
        pitch: Int,
        soundName: String
    ) {
        val delayPitch = DelayPitch(delay, pitch)
        val soundList = soundCounts.getOrDefault(soundName, mutableListOf())
        soundList.remove(delayPitch)
        soundCounts[soundName] = soundList

        if (soundList.isEmpty()) {
            soundCounts.remove(soundName)
        }

        println(soundCounts)
        println(toTag())
    }

    fun getSoundsAt(
        delay: Int,
        pitch: Int
    ): List<String> {
        val delayPitch = DelayPitch(delay, pitch)

        return soundCounts
            .filter { delayPitch in it.value }
            .keys
            .toList()
    }

    fun toTag(): Tag {
        val tag = CompoundTag()

        for ((soundString, delayPitches) in soundCounts) {

            val soundsList = tag.getList(SOUNDS, ListTag.TAG_LIST.toInt())
            for ((delay, pitch) in delayPitches) {
                val soundTag = CompoundTag()
                soundTag.putInt(DELAY, delay)
                soundTag.putInt(PITCH, pitch)

                soundsList.add(soundTag)
            }

            tag.put(soundString, soundsList)
        }

        return tag
    }

    companion object {
        private const val SOUNDS = "sounds"
        private const val DELAY = "delay"
        private const val PITCH = "pitch"

//        fun fromTag(tag: CompoundTag): ComposerTimeline? {
//
//            val timeline = ComposerTimeline()
//
//
//
//
//        }
    }

}