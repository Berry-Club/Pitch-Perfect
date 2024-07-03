package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredItem
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModCreativeTabs {

    val CREATIVE_TAB_REGISTRY: DeferredRegister<CreativeModeTab> =
        DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, PitchPerfect.ID)

    val MOD_TAB: DeferredHolder<CreativeModeTab, CreativeModeTab> =
        CREATIVE_TAB_REGISTRY.register("pitch_perfect", Supplier {
            CreativeModeTab.builder()
                .title(Component.translatable("test"))
                .icon { ModItems.BANJO.toStack() }
                .displayItems { a, b ->
                    b.acceptAll(ModItems.ITEM_REGISTRY.entries.map { (it as DeferredItem).toStack() })
                }
                .build()
        })

}