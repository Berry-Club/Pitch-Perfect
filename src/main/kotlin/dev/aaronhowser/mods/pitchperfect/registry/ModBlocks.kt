package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.block.ComposerBlock
import dev.aaronhowser.mods.pitchperfect.block.ConductorBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.state.BlockBehaviour
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks {

    val BLOCK_REGISTRY: DeferredRegister.Blocks =
        DeferredRegister.createBlocks(PitchPerfect.ID)

    val COMPOSER: DeferredBlock<Block> = registerBlock("composer") { ComposerBlock() }
    val CONDUCTOR: DeferredBlock<ConductorBlock> =
        registerBlock("conductor", makeBlockItem = false) { ConductorBlock() }

    private fun <T : Block> registerBlock(
        name: String,
        makeBlockItem: Boolean = true,
        supplier: () -> T = { Block(BlockBehaviour.Properties.of()) as T }
    ): DeferredBlock<T> {
        val block = BLOCK_REGISTRY.register(name, supplier)

        if (makeBlockItem) ModItems.ITEM_REGISTRY.registerSimpleBlockItem(name, block)

        return block
    }

}