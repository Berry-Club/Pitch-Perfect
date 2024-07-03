package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.sounds.SoundEvent
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModSounds {

    val SOUND_EVENT_REGISTRY: DeferredRegister<SoundEvent> =
        DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, PitchPerfect.ID)

    val GUITAR_SMASH: DeferredHolder<SoundEvent, SoundEvent> =
        SOUND_EVENT_REGISTRY.register("guitar_smash", Supplier {
            SoundEvent.createVariableRangeEvent(OtherUtil.modResource("guitar_smash"))
        })

}