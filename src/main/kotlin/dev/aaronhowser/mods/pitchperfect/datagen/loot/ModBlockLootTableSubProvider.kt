package dev.aaronhowser.mods.pitchperfect.datagen.loot

import dev.aaronhowser.mods.pitchperfect.registry.ModBlocks
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block

class ModBlockLootTableSubProvider(
    provider: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {

    override fun generate() {
        dropSelf(ModBlocks.CONDUCTOR.get())
        dropSelf(ModBlocks.COMPOSER.get())
    }

    override fun getKnownBlocks(): Iterable<Block> {
        return listOf(
            ModBlocks.CONDUCTOR.get(),
            ModBlocks.COMPOSER.get()
        )
    }

}