package com.aaronhowser.pitchperfect

import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModSounds {

    private val SOUND_EVENTS: DeferredRegister<SoundEvent> =
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, PitchPerfect.MOD_ID)

    val GUITAR_SMASH = registerSoundEvent("guitar_smash")

    private fun registerSoundEvent(name: String): RegistryObject<SoundEvent> {
        return SOUND_EVENTS.register(name) {
            SoundEvent(ResourceLocation(PitchPerfect.MOD_ID, name))
        }
    }

    fun register(eventBus: IEventBus) {
        SOUND_EVENTS.register(eventBus)
    }

}