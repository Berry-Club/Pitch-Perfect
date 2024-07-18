package dev.aaronhowser.mods.pitchperfect.block.entity.composer

import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.Tag
import net.minecraft.sounds.SoundEvent
import org.joml.Vector2i

class ComposerTimeline {

    private val soundCounts: MutableMap<Vector2i, MutableMap<SoundEvent, Int>> = mutableMapOf()

    private fun getSoundsAt(delay: Int, pitch: Int): MutableMap<SoundEvent, Int>? {
        require(delay >= 0) { "Delay cannot be negative" }
        require(pitch in 0..25) { "Pitch must be between 0 and 25" }

        return soundCounts[Vector2i(delay, pitch)]
    }

    fun incrementSound(delay: Int, pitch: Int, sound: SoundEvent) {
        val sounds = getSoundsAt(delay, pitch) ?: mutableMapOf()
        val current = sounds.getOrDefault(sound, 0)
        sounds[sound] = current + 1

        soundCounts[Vector2i(delay, pitch)] = sounds
    }

    fun decrementSound(delay: Int, pitch: Int, sound: SoundEvent) {
        val sounds = getSoundsAt(delay, pitch) ?: return
        val current = sounds[sound] ?: return
        sounds[sound] = current - 1

        if (sounds[sound] == 0) sounds.remove(sound)
        if (sounds.isEmpty()) soundCounts.remove(Vector2i(delay, pitch))
    }

    fun toTag(): Tag {
        val tag = CompoundTag()

        soundCounts.forEach { (vector, sounds) ->
            val soundTag = CompoundTag()
            sounds.forEach { (sound, count) ->
                val soundString = sound.location.toString()
                soundTag.putInt(soundString, count)
            }

            val delay = vector.x
            val pitch = vector.y
            val key = "$delay,$pitch"

            tag.put(key, soundTag)
        }

        return tag
    }

    companion object {

        private const val DELAY = "delay"
        private const val PITCH = "pitch"
        private const val SOUND_RL = "sound_rl"
        private const val SOUND_COUNT = "sound_count"

//        fun fromCompoundTag(compoundTag: CompoundTag): ComposerTimeline {
//
//            val timeline = ComposerTimeline()
//
//
//        }

    }

}