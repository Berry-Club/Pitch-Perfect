package dev.aaronhowser.mods.pitchperfect.event

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.packet.ModPacketHandler
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
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

}