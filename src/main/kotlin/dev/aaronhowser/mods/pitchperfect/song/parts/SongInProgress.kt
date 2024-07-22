package dev.aaronhowser.mods.pitchperfect.song.parts

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.Tag
import java.util.*

class SongInProgress(
    val uuid: UUID
) {

    constructor() : this(UUID.randomUUID())

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
        val list = soundCounts.getOrDefault(soundName, mutableListOf())
        val first = list.firstOrNull { it.matches(delay, pitch) } ?: return
        list.remove(first)
        soundCounts[soundName] = list

        if (list.isEmpty()) {
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

            val soundsList = tag.getList(SOUNDS_TAG, ListTag.TAG_LIST.toInt())
            for ((delay, pitch) in delayPitches) {
                val soundTag = CompoundTag()
                soundTag.putInt(DELAY_TAG, delay)
                soundTag.putInt(PITCH_TAG, pitch)

                soundsList.add(soundTag)
            }

            tag.put(soundString, soundsList)
        }

        return tag
    }

    companion object {
        private const val SOUNDS_TAG = "sounds"
        private const val DELAY_TAG = "delay"
        private const val PITCH_TAG = "pitch"
        private const val UUID_TAG = "uuid"

        fun fromCompoundTag(tag: CompoundTag): SongInProgress? {
            try {
                val uuid = tag.getUUID(UUID_TAG)
                val composerTimeline = SongInProgress(uuid)

                for (soundString in tag.allKeys) {
                    val soundsList = tag.get(soundString) as ListTag
                    for (soundTag in soundsList) {
                        soundTag as CompoundTag
                        val delay = soundTag.getInt(DELAY_TAG)
                        val pitch = soundTag.getInt(PITCH_TAG)

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