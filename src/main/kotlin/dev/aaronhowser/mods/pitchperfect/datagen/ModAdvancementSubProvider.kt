package dev.aaronhowser.mods.pitchperfect.datagen

import dev.aaronhowser.mods.pitchperfect.datagen.ModLanguageProvider.Companion.toComponent
import dev.aaronhowser.mods.pitchperfect.registry.ModBlocks
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.advancements.*
import net.minecraft.advancements.critereon.ImpossibleTrigger
import net.minecraft.advancements.critereon.InventoryChangeTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Items
import net.neoforged.neoforge.common.data.AdvancementProvider
import net.neoforged.neoforge.common.data.ExistingFileHelper
import java.util.function.Consumer

class ModAdvancementSubProvider : AdvancementProvider.AdvancementGenerator {

    private lateinit var registries: HolderLookup.Provider
    private lateinit var saver: Consumer<AdvancementHolder>
    private lateinit var existingFileHelper: ExistingFileHelper

    private fun guide(string: String) = OtherUtil.modResource("guide/$string")

    private fun AdvancementHolder.add(): AdvancementHolder {
        this@ModAdvancementSubProvider.saver.accept(this)
        return this
    }

    override fun generate(
        registries: HolderLookup.Provider,
        saver: Consumer<AdvancementHolder>,
        existingFileHelper: ExistingFileHelper
    ) {
        this.registries = registries
        this.saver = saver
        this.existingFileHelper = existingFileHelper

        val root = makeRoot()

        val hitMob =
            Advancement.Builder.advancement()
                .parent(root)
                .display(
                    Items.ZOMBIE_HEAD,
                    ModLanguageProvider.Advancement.HIT_MOB_TITLE.toComponent(),
                    ModLanguageProvider.Advancement.HIT_MOB_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "hit_mob",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("hit_mob"))
                .add()

        val makeComposer =
            Advancement.Builder.advancement()
                .parent(root)
                .display(
                    ModBlocks.COMPOSER.get(),
                    ModLanguageProvider.Advancement.MAKE_COMPOSER_TITLE.toComponent(),
                    ModLanguageProvider.Advancement.MAKE_COMPOSER_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "make_composer",
                    InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.COMPOSER.get())
                )
                .build(guide("make_composer"))
                .add()

        val makeConductor =
            Advancement.Builder.advancement()
                .parent(makeComposer)
                .display(
                    ModBlocks.CONDUCTOR.get(),
                    ModLanguageProvider.Advancement.MAKE_CONDUCTOR_TITLE.toComponent(),
                    ModLanguageProvider.Advancement.MAKE_CONDUCTOR_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "make_conductor",
                    InventoryChangeTrigger.TriggerInstance.hasItems(ModBlocks.CONDUCTOR.get())
                )
                .build(guide("make_conductor"))
                .add()

        val conductorComplexSong =
            Advancement.Builder.advancement()
                .parent(makeConductor)
                .display(
                    ModBlocks.CONDUCTOR.get(),
                    ModLanguageProvider.Advancement.CONDUCTOR_COMPLEX_TITLE.toComponent(),
                    ModLanguageProvider.Advancement.CONDUCTOR_COMPLEX_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "conductor_complex_song",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("conductor_complex_song"))
                .add()

        val enchantInstrument =
            Advancement.Builder.advancement()
                .parent(root)
                .display(
                    Items.ENCHANTING_TABLE,
                    ModLanguageProvider.Advancement.ENCHANT_INSTRUMENT_TITLE.toComponent(),
                    ModLanguageProvider.Advancement.ENCHANT_INSTRUMENT_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "enchant_instrument",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("enchant_instrument"))
                .add()

        val andHisMusic =
            Advancement.Builder.advancement()
                .parent(enchantInstrument)
                .display(
                    Items.LIGHTNING_ROD,
                    ModLanguageProvider.Advancement.AND_HIS_MUSIC_TITLE.toComponent(),
                    ModLanguageProvider.Advancement.AND_HIS_MUSIC_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "and_his_music_was_electric",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("and_his_music_was_electric"))
                .add()

        val healingBeat =
            Advancement.Builder.advancement()
                .parent(enchantInstrument)
                .display(
                    Items.GOLDEN_APPLE,
                    ModLanguageProvider.Advancement.HEALING_BEAT_TITLE.toComponent(),
                    ModLanguageProvider.Advancement.HEALING_BEAT_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "healing_beat",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("healing_beat"))
                .add()

        val bwaaap =
            Advancement.Builder.advancement()
                .parent(enchantInstrument)
                .display(
                    Items.TNT,
                    ModLanguageProvider.Advancement.BWAAAP_TITLE.toComponent(),
                    ModLanguageProvider.Advancement.BWAAAP_DESC.toComponent(),
                    null,
                    AdvancementType.TASK,
                    true, true, false
                )
                .addCriterion(
                    "bwaaap",
                    CriteriaTriggers.IMPOSSIBLE.createCriterion(ImpossibleTrigger.TriggerInstance())
                )
                .build(guide("bwaaap"))
                .add()

    }

    private fun makeRoot(): AdvancementHolder {
        val rootBuilder =
            Advancement.Builder.advancement()
                .display(
                    ModItems.BANJO.get(),
                    Component.literal("Pitch Perfect        "),
                    ModLanguageProvider.Advancement.ROOT_DESC.toComponent(),
                    ResourceLocation.withDefaultNamespace("textures/block/note_block.png"),
                    AdvancementType.TASK,
                    true,
                    true,
                    false
                )
                .requirements(AdvancementRequirements.Strategy.OR)

        for (instrumentItem in ModItems.instruments) {
            rootBuilder.addCriterion(
                "has_${instrumentItem.id.path}",
                InventoryChangeTrigger.TriggerInstance.hasItems(instrumentItem.get())
            )
        }

        return rootBuilder
            .build(guide("root"))
            .add()
    }
}