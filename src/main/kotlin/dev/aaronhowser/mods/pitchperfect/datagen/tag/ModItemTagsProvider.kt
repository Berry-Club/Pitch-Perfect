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

        this.tag(INSTRUMENTS_TAG)
            .add(
                ModItems.BANJO.get(),
                ModItems.BASS_DRUM.get(),
                ModItems.BASS.get(),
                ModItems.BIT.get(),
                ModItems.CHIMES.get(),
                ModItems.COW_BELL.get(),
                ModItems.DIDGERIDOO.get(),
                ModItems.ELECTRIC_PIANO.get(),
                ModItems.FLUTE.get(),
                ModItems.GLOCKENSPIEL.get(),
                ModItems.GUITAR.get(),
                ModItems.HARP.get(),
                ModItems.SNARE_DRUM.get(),
                ModItems.STICKS.get(),
                ModItems.VIBRAPHONE.get(),
                ModItems.XYLOPHONE.get()
            )

    }

}