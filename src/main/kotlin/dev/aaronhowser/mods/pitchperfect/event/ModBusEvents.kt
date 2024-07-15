package dev.aaronhowser.mods.pitchperfect.event

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import dev.aaronhowser.mods.pitchperfect.registry.ModBlockEntities
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.capabilities.Capabilities
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent

@EventBusSubscriber(
    modid = PitchPerfect.ID,
    bus = EventBusSubscriber.Bus.MOD
)
object ModBusEvents {

    @SubscribeEvent
    fun registerPayloads(event: RegisterPayloadHandlersEvent) {
        ModPacketHandler.registerPayloads(event)
    }

    @SubscribeEvent
    fun onRegisterCapabilities(event: RegisterCapabilitiesEvent) {

        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.CONDUCTOR.get()
        ) { blockEntity, direction -> blockEntity.getItemHandler(direction) }

        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            ModBlockEntities.CONDUCTOR.get()
        ) { blockEntity, direction -> blockEntity.getItemHandler(direction) }

    }

}