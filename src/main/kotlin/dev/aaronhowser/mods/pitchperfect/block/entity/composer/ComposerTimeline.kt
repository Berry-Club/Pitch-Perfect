package dev.aaronhowser.mods.pitchperfect.block.entity.composer

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag

class ComposerTimeline {

    data class DelayPitch(
        val delay: Int,
        val pitch: Int
    ) {
        fun matches(delay: Int, pitch: Int): Boolean {
            return this.delay == delay && this.pitch == pitch
        }
    }

    val soundCounts: MutableMap<String, MutableList<DelayPitch>> = mutableMapOf()

    fun addSoundAt(
        delay: Int,
        pitch: Int,
        soundName: String
    ) {
        val delayPitch = DelayPitch(delay, pitch)
        val soundList = soundCounts.getOrDefault(soundName, mutableListOf())
        soundList.add(delayPitch)
        soundCounts[soundName] = soundList
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
    }

    fun getSoundsAt(
        delay: Int,
        pitch: Int
    ): List<String> {
        val sounds = mutableListOf<String>()
        for ((sound, delayPitches) in soundCounts) {
            for (delayPitch in delayPitches) {
                if (delayPitch.matches(delay, pitch)) {
                    sounds.add(sound)
                }
            }
        }

        return sounds
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

        fun fromCompoundTag(tag: CompoundTag): ComposerTimeline? {
            try {
                val composerTimeline = ComposerTimeline()

                for (soundString in tag.allKeys) {
                    val soundsList = tag.get(soundString) as ListTag
                    for (soundTag in soundsList) {
                        soundTag as CompoundTag
                        val delay = soundTag.getInt(DELAY)
                        val pitch = soundTag.getInt(PITCH)

                        composerTimeline.addSoundAt(delay, pitch, soundString)
                    }
                }

                return composerTimeline
            } catch (e: Throwable) {
                e.printStackTrace()
                return null
            }
        }
    }

}