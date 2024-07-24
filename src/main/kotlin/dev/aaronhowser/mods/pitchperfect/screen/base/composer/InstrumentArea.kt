package dev.aaronhowser.mods.pitchperfect.screen.base.composer

import dev.aaronhowser.mods.pitchperfect.screen.ComposerScreen
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.SpriteIconButton
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class InstrumentArea(
    private val composerScreen: ComposerScreen
) {

    private val buttons: MutableList<Button> = mutableListOf()

    fun init() {
        addInstrumentButtons()
    }

    private fun addInstrumentButtons() {
        var x = composerScreen.leftPos + 5
        val y = composerScreen.topPos + 5

        for (instrument in ScreenInstrument.entries) {
            addIconButton(x, y, 18, 18, instrument.image) {
                composerScreen.selectedInstrument = if (composerScreen.selectedInstrument === instrument) {
                    null
                } else {
                    instrument
                }
            }
            x += 19
        }
    }

    private fun addIconButton(
        x: Int, y: Int,
        width: Int, height: Int,
        image: ResourceLocation,
        component: Component = Component.empty(),
        onPress: (Button) -> Unit = {}
    ) {
        val button = SpriteIconButton
            .builder(component, onPress, true)
            .sprite(image, 16, 16)
            .size(width, height)
            .build()
            .apply {
                this.x = x
                this.y = y
            }

        this.buttons.add(button)
    }

    fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        for (button in buttons) {
            button.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        }
    }

    fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int) {
        val button = buttons.firstOrNull { it.isHoveredOrFocused }

        button?.onPress()
    }

}