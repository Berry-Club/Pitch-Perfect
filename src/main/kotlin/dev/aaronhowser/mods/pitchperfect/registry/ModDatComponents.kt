package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredRegister

object ModDatComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister<DataComponentType<*>> =
        DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, PitchPerfect.ID)

}