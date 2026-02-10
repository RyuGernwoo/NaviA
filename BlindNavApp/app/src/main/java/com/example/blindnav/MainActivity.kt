package com.example.blindnav

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.blindnav.ui.MainFragment
import com.example.blindnav.ui.NavigationFragment
import com.example.blindnav.ui.WalkingFragment
import com.example.blindnav.utils.HapticUtils
import com.example.blindnav.utils.SpeechUtils
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var speechUtils: SpeechUtils
    private lateinit var hapticUtils: HapticUtils
    private lateinit var gestureDetector: GestureDetector

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        hapticUtils = HapticUtils(this)
        
        setupSpeechRecognition()
        setupGestureDetector()

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment())
                .commit()
        }

        checkPermissions()
    }

    private fun setupSpeechRecognition() {
        speechUtils = SpeechUtils(this, 
            onResult = { command ->
                handleVoiceCommand(command)
            },
            onError = {
                hapticUtils.vibrateError()
                speak("I didn't catch that. Please try again.")
            }
        )
    }

    private fun setupGestureDetector() {
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                hapticUtils.vibrateSuccess()
                speechUtils.startListening()
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event!!)
        return super.onTouchEvent(event)
    }

    private fun handleVoiceCommand(command: String) {
        val lowerCommand = command.lowercase()
        when {
            lowerCommand.contains("navigation") || lowerCommand.contains("go to") -> {
                speak("Where would you like to go?")
                navigateToFragment(NavigationFragment())
            }
            lowerCommand.contains("walking") || lowerCommand.contains("walk") -> {
                speak("Starting walking mode.")
                navigateToFragment(WalkingFragment())
            }
            lowerCommand.contains("main") || lowerCommand.contains("home") -> {
                speak("Returning to main screen.")
                navigateToFragment(MainFragment())
            }
            else -> {
                speak("Command not recognized: $command")
            }
        }
    }

    private fun navigateToFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.language = Locale.KOREA // Default to Korean as per user context
        }
    }

    fun speak(text: String) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    private fun checkPermissions() {
        val permissions = arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION)
        if (!permissions.all { ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED }) {
            ActivityCompat.requestPermissions(this, permissions, 100)
        }
    }

    override fun onDestroy() {
        if (::tts.isInitialized) tts.stop()
        if (::tts.isInitialized) tts.shutdown()
        speechUtils.destroy()
        super.onDestroy()
    }
}
