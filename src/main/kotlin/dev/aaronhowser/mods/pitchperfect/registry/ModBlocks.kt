package dev.aaronhowser.mods.pitchperfect.registry

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.block.ComposerBlock
import dev.aaronhowser.mods.pitchperfect.block.ConductorBlock
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.registries.DeferredBlock
import net.neoforged.neoforge.registries.DeferredRegister

object ModBlocks {

	val BLOCK_REGISTRY: DeferredRegister.Blocks =
		DeferredRegister.createBlocks(PitchPerfect.ID)

	val COMPOSER: DeferredBlock<ComposerBlock> =
		registerBlock("composer", ::ComposerBlock)
	val CONDUCTOR: DeferredBlock<ConductorBlock> =
		registerBlock("conductor", ::ConductorBlock, makeBlockItem = false)

	private fun <T : Block> registerBlock(
		name: String,
		supplier: () -> T,
		makeBlockItem: Boolean = true,
	): DeferredBlock<T> {
		val block = BLOCK_REGISTRY.register(name, supplier)
		if (makeBlockItem) ModItems.ITEM_REGISTRY.registerSimpleBlockItem(name, block)
		return block
	}

}