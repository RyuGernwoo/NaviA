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
        val listeningView = findViewById<android.view.View>(R.id.listening_indicator)
        
        speechUtils = SpeechUtils(this, 
            onResult = { command ->
                listeningView.visibility = android.view.View.GONE
                handleVoiceCommand(command)
            },
            onError = {
                listeningView.visibility = android.view.View.GONE
                hapticUtils.vibrateError()
                speak("음성을 인식하지 못했습니다. 다시 시도해 주세요.")
            }
        )
    }

    private fun setupGestureDetector() {
        val listeningView = findViewById<android.view.View>(R.id.listening_indicator)
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onLongPress(e: MotionEvent) {
                hapticUtils.vibrateSuccess()
                listeningView.visibility = android.view.View.VISIBLE
                speechUtils.startListening()
            }
        })
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(event!!)
        return super.onTouchEvent(event)
    }

    private fun handleVoiceCommand(command: String) {
        // Remove spaces for easier matching
        val normalized = command.replace(" ", "")
        
        when {
            normalized.contains("네비게이션") || normalized.contains("길안내") -> {
                speak("목적지를 말씀해주세요.")
                navigateToFragment(NavigationFragment.newInstance("서울역")) // Defaulting for demo
            }
            normalized.contains("보행") || normalized.contains("걷기") -> {
                speak("보행 안전 모드를 시작합니다.")
                navigateToFragment(WalkingFragment())
            }
            normalized.contains("메인") || normalized.contains("홈") || normalized.contains("처음") -> {
                speak("메인 화면으로 이동합니다.")
                navigateToFragment(MainFragment())
            }
            else -> {
                speak("알 수 없는 명령입니다: $command")
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
