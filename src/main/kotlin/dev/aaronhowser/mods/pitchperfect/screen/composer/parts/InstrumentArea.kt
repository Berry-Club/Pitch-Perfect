package dev.aaronhowser.mods.pitchperfect.screen.composer.parts

import dev.aaronhowser.mods.pitchperfect.screen.composer.ComposerScreen
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.SpriteIconButton
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation

class InstrumentArea(
    private val composerScreen: ComposerScreen
) {

    val buttons: MutableList<Button> = mutableListOf()

    fun init() {
        addInstrumentButtons()
    }

    private fun addInstrumentButtons() {
        var x = composerScreen.leftPos + 160
        val y = composerScreen.topPos + 35

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

        composerScreen.addRenderableWidgets(buttons)
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

}