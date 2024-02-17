package dev.aaronhowser.pitchperfect

import net.minecraft.resources.ResourceLocation
import net.minecraft.sounds.SoundEvent
import net.minecraftforge.eventbus.api.IEventBus
import net.minecraftforge.registries.DeferredRegister
import net.minecraftforge.registries.ForgeRegistries
import net.minecraftforge.registries.RegistryObject

object ModSounds {

    private val SOUND_EVENTS: DeferredRegister<SoundEvent> =
        DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, dev.aaronhowser.pitchperfect.PitchPerfect.MOD_ID)

    val GUITAR_SMASH = dev.aaronhowser.pitchperfect.ModSounds.registerSoundEvent("guitar_smash")

    private fun registerSoundEvent(name: String): RegistryObject<SoundEvent> {
        return dev.aaronhowser.pitchperfect.ModSounds.SOUND_EVENTS.register(name) {
            SoundEvent(ResourceLocation(dev.aaronhowser.pitchperfect.PitchPerfect.MOD_ID, name))
        }
    }

    fun register(eventBus: IEventBus) {
        dev.aaronhowser.pitchperfect.ModSounds.SOUND_EVENTS.register(eventBus)
    }

}