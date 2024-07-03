package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.component.InstrumentComponent
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModDataComponents {

    val DATA_COMPONENT_REGISTRY: DeferredRegister<DataComponentType<*>> =
        DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, PitchPerfect.ID)

    val INSTRUMENT_COMPONENT: DeferredHolder<DataComponentType<*>, DataComponentType<InstrumentComponent>> =
        DATA_COMPONENT_REGISTRY.register("instrument_component", Supplier {
            DataComponentType.builder<InstrumentComponent>()
                .persistent(InstrumentComponent.CODEC)
                .networkSynchronized(InstrumentComponent.STREAM_CODEC)
                .build()
        })

}