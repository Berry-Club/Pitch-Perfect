package dev.aaronhowser.mods.pitchperfect.registry

import net.neoforged.bus.api.IEventBus

object ModRegistries {

    private val registries = listOf(
        ModItems.ITEM_REGISTRY,
        ModCreativeTabs.CREATIVE_TAB_REGISTRY,
        ModDataComponents.DATA_COMPONENT_REGISTRY,
        ModSounds.SOUND_EVENT_REGISTRY
    )

    fun register(modBus: IEventBus) {
        registries.forEach { it.register(modBus) }
    }

}