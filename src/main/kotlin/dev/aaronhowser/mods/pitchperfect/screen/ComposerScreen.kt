package dev.aaronhowser.mods.pitchperfect.screen

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import dev.aaronhowser.mods.pitchperfect.screen.base.composer.ComposerTimeline
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.SpriteIconButton
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import kotlin.properties.Delegates

class ComposerScreen(
    val composerBlockEntity: ComposerBlockEntity,
    pTitle: Component
) : Screen(pTitle) {

    companion object {
        const val INSTRUMENT_BUTTON_SIZE = 18
        const val BUFFER_SPACE = 5
    }

    private val buttons: MutableList<Button> = mutableListOf()

    private val timeline = ComposerTimeline(this)

    var leftPos: Int by Delegates.notNull()
    var topPos: Int by Delegates.notNull()

    override fun renderBackground(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        this.renderTransparentBackground(pGuiGraphics)
        pGuiGraphics.blit(
            ScreenTextures.Background.COMPOSER,
            leftPos,
            topPos,
            0f,
            0f,
            512,
            256,
            512,
            512
        )
    }

    override fun init() {
        leftPos = (width - ScreenTextures.Background.COMPOSER_WIDTH) / 2
        topPos = (height - ScreenTextures.Background.COMPOSER_HEIGHT) / 2

        addInstrumentButtons()
        timeline.addTimelineButtons()
    }

    private enum class Instruments(
        val image: ResourceLocation
    ) {
        BANJO(ScreenTextures.Sprite.Instrument.BANJO),
        BASS(ScreenTextures.Sprite.Instrument.BASS),
        BASS_DRUM(ScreenTextures.Sprite.Instrument.BASS_DRUM),
        BIT(ScreenTextures.Sprite.Instrument.BIT),
        CHIMES(ScreenTextures.Sprite.Instrument.CHIMES),
        COW_BELL(ScreenTextures.Sprite.Instrument.COW_BELL),
        DIDGERIDOO(ScreenTextures.Sprite.Instrument.DIDGERIDOO),
        ELECTRIC_PIANO(ScreenTextures.Sprite.Instrument.ELECTRIC_PIANO),
        FLUTE(ScreenTextures.Sprite.Instrument.FLUTE),
        GLOCKENSPIEL(ScreenTextures.Sprite.Instrument.GLOCKENSPIEL),
        GUITAR(ScreenTextures.Sprite.Instrument.GUITAR),
        HARP(ScreenTextures.Sprite.Instrument.HARP),
        SNARE_DRUM(ScreenTextures.Sprite.Instrument.SNARE_DRUM),
        STICKS(ScreenTextures.Sprite.Instrument.STICKS),
        VIBRAPHONE(ScreenTextures.Sprite.Instrument.VIBRAPHONE),
        XYLOPHONE(ScreenTextures.Sprite.Instrument.XYLOPHONE)
    }

    private fun addInstrumentButtons() {
        var x = leftPos + 5
        val y = topPos + 5

        for (instrument in Instruments.entries) {
            addIconButton(x, y, 18, 18, instrument.image)
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

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)

        renderButtons(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    private fun renderButtons(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        for (button in buttons) {
            button.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        }

        timeline.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }


}