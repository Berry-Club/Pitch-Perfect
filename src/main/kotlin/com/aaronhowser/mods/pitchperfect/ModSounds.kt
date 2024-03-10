package com.aaronhowser.mods.pitchperfect

import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModSounds {

    private val SOUND_EVENTS: DeferredRegister<SoundEvent> =
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, com.aaronhowser.mods.pitchperfect.PitchPerfect.MOD_ID)

    val GUITAR_SMASH = com.aaronhowser.mods.pitchperfect.ModSounds.registerSoundEvent("guitar_smash")

    private fun registerSoundEvent(name: String): RegistryObject<SoundEvent> {
        return com.aaronhowser.mods.pitchperfect.ModSounds.SOUND_EVENTS.register(name) {
            SoundEvent(ResourceLocation(com.aaronhowser.mods.pitchperfect.PitchPerfect.MOD_ID, name))
        }
    }

    fun register(eventBus: IEventBus) {
        com.aaronhowser.mods.pitchperfect.ModSounds.SOUND_EVENTS.register(eventBus)
    }

}