package dev.aaronhowser.mods.pitchperfect.datagen

import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.data.recipes.RecipeOutput
import net.minecraft.data.recipes.RecipeProvider
import net.minecraft.data.recipes.ShapedRecipeBuilder
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.Tags
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>
) : RecipeProvider(output, lookupProvider) {

    override fun buildRecipes(pRecipeOutput: RecipeOutput) {
        banjo.save(pRecipeOutput)
        bass.save(pRecipeOutput)
        bassDrum.save(pRecipeOutput)
        bit.save(pRecipeOutput)
        chimes.save(pRecipeOutput)
        cowBell.save(pRecipeOutput)
        didgeridoo.save(pRecipeOutput)
        electricPiano.save(pRecipeOutput)
        flute.save(pRecipeOutput)
        glockenspiel.save(pRecipeOutput)
        guitar.save(pRecipeOutput)
        harp.save(pRecipeOutput)
        snareDrum.save(pRecipeOutput)
        sticks.save(pRecipeOutput)
        vibraphone.save(pRecipeOutput)
        xylophone.save(pRecipeOutput)
    }

    companion object {

        private val banjo =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BANJO.get())
                .pattern("  S")
                .pattern("WS ")
                .pattern("LW ")
                .define('S', Items.STICK)
                .define('W', ItemTags.PLANKS)
                .define('L', ItemTags.WOOL)
                .unlockedBy("has_stick", has(Items.STICK))

        private val bass =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BASS.get())
                .pattern("  S")
                .pattern("WS ")
                .pattern("NW ")
                .define('S', Items.STICK)
                .define('N', Items.NOTE_BLOCK)
                .define('W', ItemTags.PLANKS)
                .unlockedBy("has_note_block", has(Items.NOTE_BLOCK))

        private val bassDrum =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BASS_DRUM.get())
                .pattern("IWI")
                .pattern("LWW")
                .pattern("IWI")
                .define('I', Items.IRON_NUGGET)
                .define('W', ItemTags.WOOL)
                .define('L', Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))

        private val bit =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BIT.get())
                .pattern("III")
                .pattern("IGI")
                .pattern("IRI")
                .define('I', Items.IRON_NUGGET)
                .define('G', Items.GREEN_STAINED_GLASS_PANE)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_redstone", has(Items.REDSTONE))

        private val chimes =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CHIMES.get())
                .pattern(" S ")
                .pattern("SSS")
                .pattern("GGG")
                .define('S', Items.STRING)
                .define('G', Items.GOLD_NUGGET)
                .unlockedBy("has_gold_nugget", has(Items.GOLD_NUGGET))

        private val cowBell =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COW_BELL.get())
                .pattern(" I ")
                .pattern("IBI")
                .pattern("I I")
                .define('I', Items.IRON_NUGGET)
                .define('B', Items.BELL)
                .unlockedBy("has_bell", has(Items.BELL))

        private val didgeridoo =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.DIDGERIDOO.get())
                .pattern("  F")
                .pattern(" W ")
                .pattern("W  ")
                .define('F', ModItems.FLUTE.get())
                .define('W', Items.STICK)
                .unlockedBy("has_flute", has(ModItems.FLUTE.get()))

        private val electricPiano =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.ELECTRIC_PIANO.get())
                .pattern("BBB")
                .pattern("DDD")
                .pattern("BRB")
                .define('B', Items.BLACK_CONCRETE)
                .define('D', Items.BONE_BLOCK)
                .define('R', Items.REDSTONE)
                .unlockedBy("has_bone_block", has(Items.BONE_BLOCK))

        private val flute =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FLUTE.get())
                .pattern(" WS")
                .pattern("SBS")
                .pattern("SW ")
                .define('S', Items.STICK)
                .define('B', Items.BAMBOO)
                .define('W', ItemTags.PLANKS)
                .unlockedBy("has_bamboo", has(Items.BAMBOO))

        private val glockenspiel =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GLOCKENSPIEL.get())
                .pattern("SSS")
                .pattern("I I")
                .pattern("SSS")
                .define('I', Items.IRON_NUGGET)
                .define('S', Items.STICK)
                .unlockedBy("has_stick", has(Items.STICK))

        private val guitar =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GUITAR.get())
                .pattern("  S")
                .pattern("WS ")
                .pattern("TW ")
                .define('S', Items.STICK)
                .define('W', ItemTags.PLANKS)
                .define('T', Items.STRING)
                .unlockedBy("has_string", has(Items.STRING))

        private val harp =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.HARP.get())
                .pattern("GNN")
                .pattern("G S")
                .pattern("GNN")
                .define('G', Items.GOLD_INGOT)
                .define('N', Items.GOLD_NUGGET)
                .define('S', Items.STRING)
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))

        private val snareDrum =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SNARE_DRUM.get())
                .pattern(" S ")
                .pattern("ILI")
                .pattern("IWI")
                .define('S', ModItems.STICKS.get())
                .define('I', Items.IRON_NUGGET)
                .define('W', ItemTags.WOOL)
                .define('L', Items.LEATHER)
                .unlockedBy("has_leather", has(Items.LEATHER))

        private val sticks =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.STICKS.get())
                .pattern("P P")
                .pattern(" S ")
                .pattern("S S")
                .define('P', ItemTags.PLANKS)
                .define('S', Items.STICK)
                .unlockedBy("has_stick", has(Items.STICK))

        private val vibraphone =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.VIBRAPHONE.get())
                .pattern(" G ")
                .pattern("IBI")
                .pattern("I I")
                .define('G', ModItems.GLOCKENSPIEL.get())
                .define('I', Items.IRON_INGOT)
                .define('B', Items.BELL)
                .unlockedBy("has_bell", has(Items.BELL))

        private val xylophone =
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.XYLOPHONE.get())
                .pattern("SSS")
                .pattern("PDP")
                .pattern("SSS")
                .define('P', ItemTags.PLANKS)
                .define('S', Items.STICK)
                .define('D', Tags.Items.DYES)
                .unlockedBy("has_stick", has(Items.STICK))

    }

}