package dev.aaronhowser.mods.pitchperfect.datagen.loot

import dev.aaronhowser.mods.pitchperfect.registry.ModBlocks
import dev.aaronhowser.mods.pitchperfect.registry.ModDataComponents
import net.minecraft.core.HolderLookup
import net.minecraft.data.loot.BlockLootSubProvider
import net.minecraft.world.flag.FeatureFlags
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.storage.loot.LootPool
import net.minecraft.world.level.storage.loot.LootTable
import net.minecraft.world.level.storage.loot.entries.LootItem
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue

class ModBlockLootTableSubProvider(
    provider: HolderLookup.Provider
) : BlockLootSubProvider(setOf(), FeatureFlags.REGISTRY.allFlags(), provider) {

    override fun generate() {
        dropSelf(ModBlocks.CONDUCTOR.get())
        composer()
    }

    private fun composer() {
        val composer = ModBlocks.COMPOSER.get()
        val builder = LootTable.lootTable()
            .withPool(
                applyExplosionCondition(
                    composer,
                    LootPool.lootPool()
                        .setRolls(ConstantValue.exactly(1f))
                        .add(
                            LootItem.lootTableItem(composer)
                                .apply(
                                    CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                        .include(ModDataComponents.COMPOSER_SONG_COMPONENT.get())
                                )
                        )
                )
            )

        this.add(composer, builder)
    }

    override fun getKnownBlocks(): Iterable<Block> {
        return listOf(
            ModBlocks.CONDUCTOR.get(),
            ModBlocks.COMPOSER.get()
        )
    }

}