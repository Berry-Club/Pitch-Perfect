package dev.aaronhowser.mods.pitchperfect.screen.base

import dev.aaronhowser.mods.pitchperfect.util.OtherUtil

object ScreenTextures {

    object Background {

        object Composer {
            val COMPOSER = OtherUtil.modResource("textures/gui/container/composer.png")

            const val CANVAS_SIZE = 512

            const val WIDTH = 455
            const val HEIGHT = 256
        }
    }

    object Sprite {

        object StepJump {
            val STEP_JUMPER = OtherUtil.modResource("step_jump")

            const val CANVAS_SIZE = 16

            const val WIDTH = 7
            const val HEIGHT = 3
        }

        object Instrument {
            val BANJO = OtherUtil.modResource("instruments/banjo")
            val BASS = OtherUtil.modResource("instruments/bass")
            val BASS_DRUM = OtherUtil.modResource("instruments/bass_drum")
            val BIT = OtherUtil.modResource("instruments/bit")
            val CHIMES = OtherUtil.modResource("instruments/chimes")
            val COW_BELL = OtherUtil.modResource("instruments/cow_bell")
            val DIDGERIDOO = OtherUtil.modResource("instruments/didgeridoo")
            val ELECTRIC_PIANO = OtherUtil.modResource("instruments/electric_piano")
            val FLUTE = OtherUtil.modResource("instruments/flute")
            val GLOCKENSPIEL = OtherUtil.modResource("instruments/glockenspiel")
            val GUITAR = OtherUtil.modResource("instruments/guitar")
            val HARP = OtherUtil.modResource("instruments/harp")
            val SNARE_DRUM = OtherUtil.modResource("instruments/snare_drum")
            val STICKS = OtherUtil.modResource("instruments/sticks")
            val VIBRAPHONE = OtherUtil.modResource("instruments/vibraphone")
            val XYLOPHONE = OtherUtil.modResource("instruments/xylophone")
        }

    }

}