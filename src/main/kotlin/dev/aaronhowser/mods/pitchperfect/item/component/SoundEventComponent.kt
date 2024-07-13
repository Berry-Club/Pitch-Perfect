package dev.aaronhowser.mods.pitchperfect.item.component

import com.mojang.serialization.Codec
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import io.netty.buffer.ByteBuf
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.network.codec.StreamCodec
import net.minecraft.sounds.SoundEvent
import net.minecraft.world.item.ItemStack

data class SoundEventComponent(
    val soundEvent: SoundEvent
) {

    companion object {

        val CODEC: Codec<SoundEventComponent> =
            BuiltInRegistries.SOUND_EVENT.byNameCodec()
                .xmap(::SoundEventComponent, SoundEventComponent::soundEvent)

        val STREAM_CODEC: StreamCodec<ByteBuf, SoundEventComponent> =
            ByteBufCodecs.fromCodec(CODEC)

        val component: DataComponentType<SoundEventComponent> by lazy {
            ModDataComponents.SOUND_EVENT_COMPONENT.get()
        }

        fun getSoundEvent(itemStack: ItemStack): SoundEvent? {
            return itemStack.get(component)?.soundEvent
        }

    }

}