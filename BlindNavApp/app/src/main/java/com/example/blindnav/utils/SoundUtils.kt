package com.example.blindnav.utils

import android.media.AudioManager
import android.media.ToneGenerator

class SoundUtils {
    private val toneGenerator = ToneGenerator(AudioManager.STREAM_MUSIC, 100)

    fun playListeningStart() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_PROMPT, 200)
    }

    fun playSuccess() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_ACK, 200)
    }

    fun playError() {
        toneGenerator.startTone(ToneGenerator.TONE_PROP_NACK, 200) // Low pitch error
    }
    
    fun release() {
        toneGenerator.release()
    }
}
