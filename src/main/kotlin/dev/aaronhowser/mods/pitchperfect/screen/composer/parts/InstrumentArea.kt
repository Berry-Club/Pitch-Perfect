package dev.aaronhowser.mods.pitchperfect.screen.composer.parts

import dev.aaronhowser.mods.pitchperfect.screen.base.ToggleButton
import dev.aaronhowser.mods.pitchperfect.screen.composer.ComposerScreen
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics

class InstrumentArea(
	private val composerScreen: ComposerScreen,
	private val font: Font
) {

	fun init() {
		addInstrumentButtons()
	}

	private val buttons = mutableListOf<ToggleButton>()

	private fun addInstrumentButtons() {
		buttons.clear()

		var y = composerScreen.timeline.topPos
		val buttonSize = 20
		var isLeft = true

		for (i in ScreenInstrument.entries.indices) {
			val instrument = ScreenInstrument.entries[i]

			val x = if (isLeft) {
				composerScreen.timeline.rightPos + 5
			} else {
				composerScreen.timeline.rightPos + 5 + buttonSize + 2
			}

			val button = ToggleButton(
				buttonSize, buttonSize,
				instrument.displayName, instrument.displayName,
				16, 16,
				instrument.image,
				{ btn ->
					composerScreen.selectedInstrument = if (composerScreen.selectedInstrument === instrument) {
						null
					} else {
						instrument
					}
					deselectAllOthers()
				},
				null
			)

			button.x = x
			button.y = y

			buttons.add(button)
			composerScreen.addRenderableWidgets(button)

			if (!isLeft) {
				y += buttonSize + 2
			}
			isLeft = !isLeft
		}
	}

	private fun deselectAllOthers() {
		for (button in buttons) {
			button.toggledOn = button.message == composerScreen.selectedInstrument?.displayName
		}
	}

	fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
		val instrument = composerScreen.selectedInstrument ?: return
		val component = instrument.displayName

		pGuiGraphics.drawString(
			font,
			component,
			composerScreen.timeline.rightPos + 5,
			composerScreen.timeline.topPos - 10,
			0xFFFFFF
		)
	}

}