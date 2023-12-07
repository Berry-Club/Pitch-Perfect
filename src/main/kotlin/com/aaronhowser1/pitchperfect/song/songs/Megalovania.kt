package com.aaronhowser1.pitchperfect.song.songs

import com.aaronhowser1.pitchperfect.item.ModItems
import com.aaronhowser1.pitchperfect.song.NoteSequence
import com.aaronhowser1.pitchperfect.song.SongRegistry
import com.aaronhowser1.pitchperfect.utils.CommonUtils.asInstrument

object Megalovania {

    fun register(): NoteSequence = SongRegistry.song("megalovania", ModItems.BIT.asInstrument()!!) {
        beat {
            notes(8)
        }
        beat {
            notes(8)
        }
        beat {
            notes(20)
        }
        beat {
            notes(15)
        }
        beat {
            notes(14)
        }
        beat {
            notes(13)
        }
        beat {
            notes(11)
        }
        beat {
            notes(8)
        }
        beat {
            notes(11)
        }
        beat {
            notes(13)
        }
        beat {
            notes(6)
        }

    }

}