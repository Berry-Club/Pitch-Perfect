package dev.aaronhowser.pitchperfect.utils

import dev.aaronhowser.pitchperfect.config.ClientConfig
import dev.aaronhowser.pitchperfect.utils.CommonUtils.map
import net.minecraft.client.Minecraft
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.core.particles.SimpleParticleType
import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundSource
import net.minecraft.world.level.Level
import net.minecraftforge.registries.ForgeRegistries

object ClientUtils {

    private val level: Level
        get() = Minecraft.getInstance().level!!

    private val player
        get() = Minecraft.getInstance().player!!

    fun spawnParticle(
        particleType: SimpleParticleType,
        x: Double,
        y: Double,
        z: Double,
        red: Float,
        green: Float,
        blue: Float
    ) {
        level.addParticle(
            particleType,
            x, y, z,
            red.toDouble(), green.toDouble(), blue
                .toDouble()
        )
    }

    fun playNote(
        soundResourceLocation: ResourceLocation,
        pitch: Float,
        x: Double,
        y: Double,
        z: Double,
        hasBwaaap: Boolean = false
    ) {

        val noteColor = pitch.map(0.5f, 2f, 0f, 24f)
        spawnParticle(
            ParticleTypes.NOTE,
            x, y, z,
            noteColor / 24, 0f, 0f
        )

        val sound = ForgeRegistries.SOUND_EVENTS.getValue(soundResourceLocation)!!

        val volume = if (hasBwaaap) {
            ClientConfig.VOLUME.get().toFloat() * 1.5f
        } else {
            ClientConfig.VOLUME.get().toFloat()
        }

        level.playSound(
            player,
            x, y, z,
            sound,
            SoundSource.PLAYERS,
            volume,
            pitch
        )
    }

}