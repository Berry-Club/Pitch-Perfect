package dev.aaronhowser.mods.pitchperfect.screen.composer.parts

import dev.aaronhowser.mods.pitchperfect.screen.base.ToggleButton
import dev.aaronhowser.mods.pitchperfect.screen.composer.ComposerScreen
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

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

            addIconButton(
                x, y,
                buttonSize, buttonSize,
                instrument.image,
                instrument.displayName,
            ) {
                composerScreen.selectedInstrument = if (composerScreen.selectedInstrument === instrument) {
                    null
                } else {
                    instrument
                }

                deselectAllOthers()
            }

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

    private fun addIconButton(
        x: Int, y: Int,
        width: Int, height: Int,
        image: ResourceLocation,
        messageOn: Component,
        messageOff: Component = messageOn,
        onPress: (Button) -> Unit = {}
    ) {
        val button = ToggleButton(
            width, height,
            messageOn, messageOff,
            16, 16,
            image,
            onPress,
            null
        ).apply {
            this.x = x
            this.y = y
        }

        buttons.add(button)

        composerScreen.addRenderableWidgets(button)
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