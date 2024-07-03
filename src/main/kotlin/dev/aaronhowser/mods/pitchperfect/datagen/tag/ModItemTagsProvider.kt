package dev.aaronhowser.mods.pitchperfect.datagen.tag

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.tags.ItemTagsProvider
import net.minecraft.tags.TagKey
import net.minecraft.world.item.Item
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.concurrent.CompletableFuture

class ModItemTagsProvider(
    pOutput: PackOutput,
    pLookupProvider: CompletableFuture<HolderLookup.Provider>,
    pBlockTags: CompletableFuture<TagLookup<Block>>,
    existingFileHelper: ExistingFileHelper?
) : ItemTagsProvider(pOutput, pLookupProvider, pBlockTags, PitchPerfect.ID, existingFileHelper) {

    companion object {
        val INSTRUMENTS_TAG: TagKey<Item> = TagKey.create(Registries.ITEM, OtherUtil.modResource("instruments"))
    }

    override fun addTags(pProvider: HolderLookup.Provider) {

        //TODO: If I ever add blocks (or items that aren't Instruments), exclude them from this tag
        for (item in ModItems.ITEM_REGISTRY.entries) {
            this.tag(INSTRUMENTS_TAG)
                .add(item.get())
        }

    }

}