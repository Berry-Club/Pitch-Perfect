package dev.aaronhowser.mods.pitchperfect.datagen.language

import dev.aaronhowser.mods.pitchperfect.PitchPerfect
import dev.aaronhowser.mods.pitchperfect.item.InstrumentItem
import dev.aaronhowser.mods.pitchperfect.util.OtherUtil
import net.minecraft.ChatFormatting
import net.minecraft.data.PackOutput
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.MutableComponent
import net.minecraft.network.chat.Style
import net.neoforged.neoforge.common.data.LanguageProvider

class ModLanguageProvider(output: PackOutput) : LanguageProvider(output, PitchPerfect.ID, "en_us") {

	object FontIcon {
		val FONT = OtherUtil.modResource("icons")
		private val STYLE = Style.EMPTY.withFont(FONT).withColor(ChatFormatting.WHITE)

		fun getIcon(instrumentItem: InstrumentItem): MutableComponent {
			val fontString = instrumentItem.fontString
			return Component.literal(fontString).setStyle(STYLE)
		}
	}

	object Block {
		const val COMPOSER = "block.pitchperfect.composer"
		const val CONDUCTOR = "block.pitchperfect.conductor"
	}

	object Enchantment {
		const val HEALING_BEAT = "enchantment.pitchperfect.healing_beat"
		const val HEALING_BEAT_DESC = "enchantment.pitchperfect.healing_beat.desc"
		const val BWAAAP = "enchantment.pitchperfect.bwaaap"
		const val BWAAAP_DESC = "enchantment.pitchperfect.bwaaap.desc"
		const val AND_HIS_MUSIC_WAS_ELECTRIC = "enchantment.pitchperfect.and_his_music_was_electric"
		const val AND_HIS_MUSIC_WAS_ELECTRIC_DESC = "enchantment.pitchperfect.and_his_music_was_electric.desc"
	}

	object Misc {
		const val CREATIVE_TAB = "itemGroup.pitchperfect"

		const val SONG_INFO = "pitchperfect.song_info"
		const val SONG_AUTHORS = "pitchperfect.song_authors"
		const val SONG_UUID = "pitchperfect.song_uuid"
		const val SONG_RAW = "pitchperfect.song_raw"
		const val SONG_PLAY = "pitchperfect.song_play"
	}

	companion object {
		fun String.toComponent(vararg args: Any): MutableComponent = Component.translatable(this, *args)
	}

	override fun addTranslations() {
		ModItemLang.add(this)
		ModTooltipLang.add(this)
		ModAdvancementLang.add(this)
		ModMessageLang.add(this)

		add(Block.COMPOSER, "Composer")
		add(Block.CONDUCTOR, "Conductor")

		add(Enchantment.HEALING_BEAT, "Healing Beat")
		add(Enchantment.HEALING_BEAT_DESC, "Heals nearby mobs when used")
		add(Enchantment.BWAAAP, "BWAAAP")
		add(Enchantment.BWAAAP_DESC, "Knocks back nearby mobs when used")
		add(Enchantment.AND_HIS_MUSIC_WAS_ELECTRIC, "And His Music Was Electric")
		add(Enchantment.AND_HIS_MUSIC_WAS_ELECTRIC_DESC, "When anywhere in your inventory,\ndealing damage will chain to other mobs\nNOTE: Damages the item")

		add(Misc.CREATIVE_TAB, "Pitch Perfect")
		add(Misc.SONG_INFO, "%1\$s - %2\$s %3\$s %4\$s %5\$s")
		add(Misc.SONG_AUTHORS, "[Authors]")
		add(Misc.SONG_UUID, "[UUID]")
		add(Misc.SONG_RAW, "[Raw]")
		add(Misc.SONG_PLAY, "[Play]")
	}
}