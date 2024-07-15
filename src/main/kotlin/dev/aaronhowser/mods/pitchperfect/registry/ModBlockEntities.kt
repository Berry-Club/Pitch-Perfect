package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.block.entity.ConductorBlockEntity
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.registries.DeferredHolder
import net.neoforged.neoforge.registries.DeferredRegister
import java.util.function.Supplier

object ModBlockEntities {

    val BLOCK_ENTITY_REGISTRY: DeferredRegister<BlockEntityType<*>> =
        DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, PitchPerfect.ID)

    val COMPOSER: DeferredHolder<BlockEntityType<*>, BlockEntityType<ComposerBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("composer", Supplier {
            BlockEntityType.Builder.of(
                { pos, state -> ComposerBlockEntity(pos, state) },
                ModBlocks.COMPOSER.get()
            ).build(null)
        })

    val CONDUCTOR: DeferredHolder<BlockEntityType<*>, BlockEntityType<ConductorBlockEntity>> =
        BLOCK_ENTITY_REGISTRY.register("conductor", Supplier {
            BlockEntityType.Builder.of(
                { pos, state -> ConductorBlockEntity(pos, state) },
                ModBlocks.CONDUCTOR.get()
            ).build(null)
        })

}