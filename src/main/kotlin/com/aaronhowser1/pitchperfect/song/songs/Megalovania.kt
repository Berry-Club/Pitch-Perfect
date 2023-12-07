package com.aaronhowser1.pitchperfect.song.songs

import com.aaronhowser1.pitchperfect.item.ModItems
import com.aaronhowser1.pitchperfect.song.NoteSequence
import com.aaronhowser1.pitchperfect.song.SongRegistry
import com.aaronhowser1.pitchperfect.utils.CommonUtils.asInstrument

object Megalovania {

    fun register(): NoteSequence = SongRegistry.song("megalovania", ModItems.BIT.asInstrument()!!) {
        beat {
            notes(8)
            repeaterTicksAfter = 2
        }
        beat {
            notes(8)
        }
        beat {
            notes(20)
            repeaterTicksAfter = 2
        }
        beat {
            notes(15)
            repeaterTicksAfter = 3
        }
        beat {
            notes(14)
            repeaterTicksAfter = 3
        }
        beat {
            notes(13)
            repeaterTicksAfter = 2
        }
        beat {
            notes(11)
            repeaterTicksAfter = 2
        }
        beat {
            notes(8)
        }
        beat {
            notes(11)
        }
        beat {
            notes(13)
            repeaterTicksAfter = 2
        }
        beat {
            notes(6)
        }
        beat {
            notes(6)
        }
        beat {
            notes(20)
            repeaterTicksAfter = 2
        }
        beat {
            notes(15)
            repeaterTicksAfter = 3
        }
        beat {
            notes(14)
            repeaterTicksAfter = 3
        }
        beat {
            notes(13)
            repeaterTicksAfter = 2
        }
        beat {
            notes(11)
            repeaterTicksAfter = 2
        }
        beat {
            notes(11, 8)
        }
        beat {
            notes(13)
            repeaterTicksAfter = 2
        }
        beat {
            notes(5)
            repeaterTicksAfter = 2
        }
        beat {
            notes(5)
            repeaterTicksAfter = 2
        }
        beat {
            notes(20)
            repeaterTicksAfter = 2
        }

    }

}