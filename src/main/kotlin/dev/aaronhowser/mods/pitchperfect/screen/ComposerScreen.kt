package dev.aaronhowser.mods.pitchperfect.screen

import dev.aaronhowser.mods.pitchperfect.block.entity.ComposerBlockEntity
import dev.aaronhowser.mods.pitchperfect.item.InstrumentItem
import dev.aaronhowser.mods.pitchperfect.registry.ModItems
import dev.aaronhowser.mods.pitchperfect.screen.base.ScreenTextures
import dev.aaronhowser.mods.pitchperfect.screen.base.composer.timeline.Timeline
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.components.Button
import net.minecraft.client.gui.components.SpriteIconButton
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import net.neoforged.api.distmarker.Dist
import net.neoforged.api.distmarker.OnlyIn
import kotlin.properties.Delegates

@OnlyIn(Dist.CLIENT)
class ComposerScreen(
    val composerBlockEntity: ComposerBlockEntity,
    pTitle: Component
) : Screen(pTitle) {

    companion object {
        const val INSTRUMENT_BUTTON_SIZE = 18
        const val BUFFER_SPACE = 5
    }

    var selectedInstrument: Instrument? = null

    private val instrumentButtons: MutableList<Button> = mutableListOf()
    private val timeline by lazy { Timeline(this, this.font) }
    private val buttons: List<Button> by lazy { instrumentButtons }

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

        timeline.init()
        addInstrumentButtons()
    }

    enum class Instrument(
        val image: ResourceLocation,
        val instrumentItem: InstrumentItem
    ) {
        BANJO(ScreenTextures.Sprite.Instrument.BANJO, ModItems.BANJO.get()),
        BASS(ScreenTextures.Sprite.Instrument.BASS, ModItems.BASS.get()),
        BASS_DRUM(ScreenTextures.Sprite.Instrument.BASS_DRUM, ModItems.BASS_DRUM.get()),
        BIT(ScreenTextures.Sprite.Instrument.BIT, ModItems.BIT.get()),
        CHIMES(ScreenTextures.Sprite.Instrument.CHIMES, ModItems.CHIMES.get()),
        COW_BELL(ScreenTextures.Sprite.Instrument.COW_BELL, ModItems.COW_BELL.get()),
        DIDGERIDOO(ScreenTextures.Sprite.Instrument.DIDGERIDOO, ModItems.DIDGERIDOO.get()),
        ELECTRIC_PIANO(ScreenTextures.Sprite.Instrument.ELECTRIC_PIANO, ModItems.ELECTRIC_PIANO.get()),
        FLUTE(ScreenTextures.Sprite.Instrument.FLUTE, ModItems.FLUTE.get()),
        GLOCKENSPIEL(ScreenTextures.Sprite.Instrument.GLOCKENSPIEL, ModItems.GLOCKENSPIEL.get()),
        GUITAR(ScreenTextures.Sprite.Instrument.GUITAR, ModItems.GUITAR.get()),
        HARP(ScreenTextures.Sprite.Instrument.HARP, ModItems.HARP.get()),
        SNARE_DRUM(ScreenTextures.Sprite.Instrument.SNARE_DRUM, ModItems.SNARE_DRUM.get()),
        STICKS(ScreenTextures.Sprite.Instrument.STICKS, ModItems.STICKS.get()),
        VIBRAPHONE(ScreenTextures.Sprite.Instrument.VIBRAPHONE, ModItems.VIBRAPHONE.get()),
        XYLOPHONE(ScreenTextures.Sprite.Instrument.XYLOPHONE, ModItems.XYLOPHONE.get())
    }

    private fun addInstrumentButtons() {
        var x = leftPos + 5
        val y = topPos + 5

        for (instrument in Instrument.entries) {
            addIconButton(x, y, 18, 18, instrument.image) {
                selectedInstrument = if (selectedInstrument === instrument) {
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

        this.instrumentButtons.add(button)
    }

    override fun render(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)

        renderButtons(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    private fun renderButtons(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        renderInstrumentButtons(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        timeline.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
    }

    private fun renderInstrumentButtons(pGuiGraphics: GuiGraphics, pMouseX: Int, pMouseY: Int, pPartialTick: Float) {
        for (button in instrumentButtons) {
            button.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick)
        }
    }

    override fun mouseClicked(pMouseX: Double, pMouseY: Double, pButton: Int): Boolean {
        timeline.mouseClicked(pMouseX, pMouseY, pButton)

        val button = buttons.firstOrNull { it.isHoveredOrFocused }
        if (button == null) return false

        button.onPress()
        return true
    }

    override fun mouseScrolled(pMouseX: Double, pMouseY: Double, pScrollX: Double, pScrollY: Double): Boolean {
        if (pScrollY > 0) {
            timeline.verticalScrollIndex--
        } else if (pScrollY < 0) {
            timeline.verticalScrollIndex++
        }

        if (pScrollX > 0) {
            timeline.horizontalScrollIndex--
        } else if (pScrollX < 0) {
            timeline.horizontalScrollIndex++
        }

        return super.mouseScrolled(pMouseX, pMouseY, pScrollX, pScrollY)
    }

}