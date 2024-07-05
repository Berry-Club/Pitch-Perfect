package dev.aaronhowser.mods.pitchperfect.util

import dev.aaronhowser.mods.pitchperfect.config.ClientConfig
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil.map
import net.minecraft.client.Minecraft
import net.minecraft.client.player.LocalPlayer
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.Level

object ClientUtil {

    val localPlayer: LocalPlayer?
        get() = Minecraft.getInstance().player

    val clientLevel: Level?
        get() = localPlayer?.level()

    fun playNote(
        soundRl: ResourceLocation,
        pitch: Float,
        x: Double,
        y: Double,
        z: Double,
        hasBwaaap: Boolean
    ) {

        val noteColor = pitch.map(0.5f, 2f, 0f, 24f)

        spawnParticle(
            ParticleTypes.NOTE,
            x, y, z,
            noteColor / 24f, 0f, 0f
        )

        val sound =
            BuiltInRegistries.SOUND_EVENT.get(soundRl) ?: throw IllegalArgumentException("Sound not found: $soundRl")

        val volume = if (hasBwaaap) {
            ClientConfig.VOLUME.get().toFloat() * 1.5f
        } else {
            ClientConfig.VOLUME.get().toFloat()
        }

        clientLevel?.playSound(
            localPlayer,
            x, y, z,
            sound,
            SoundSource.PLAYERS,
            volume,
            pitch
        )

    }

    fun spawnParticle(
        particleType: SimpleParticleType,
        x: Double,
        y: Double,
        z: Double,
        red: Float,
        green: Float,
        blue: Float
    ) {
        clientLevel?.addParticle(
            particleType,
            true,
            x, y, z,
            red.toDouble(),
            green.toDouble(),
            blue.toDouble()
        )
    }

}