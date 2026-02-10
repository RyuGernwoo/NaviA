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
import com.example.blindnav.utils.SoundUtils
import java.util.Locale

class MainActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var speechUtils: SpeechUtils
    private lateinit var hapticUtils: HapticUtils
    private lateinit var gestureDetector: GestureDetector
    private lateinit var soundUtils: SoundUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tts = TextToSpeech(this, this)
        hapticUtils = HapticUtils(this)
        soundUtils = SoundUtils()
        
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
                soundUtils.playSuccess()
                handleVoiceCommand(command)
            },
            onError = { errorCode ->
                listeningView.visibility = android.view.View.GONE
                hapticUtils.vibrateError()
                soundUtils.playError()
                
                val message = when (errorCode) {
                    android.speech.SpeechRecognizer.ERROR_NO_MATCH -> "말씀하신 내용을 이해하지 못했습니다."
                    android.speech.SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "입력이 없어 인식을 종료합니다."
                    android.speech.SpeechRecognizer.ERROR_NETWORK, android.speech.SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트워크 연결을 확인해주세요."
                    else -> "음성 인식 중 오류가 발생했습니다. 다시 시도해 주세요."
                }
                speak(message)
            }
        )
    }

    private fun setupGestureDetector() {
        val listeningView = findViewById<android.view.View>(R.id.listening_indicator)
        gestureDetector = GestureDetector(this, object : GestureDetector.SimpleOnGestureListener() {
            override fun onDown(e: MotionEvent): Boolean {
                hapticUtils.vibrateTick() // Tick on touch down
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                hapticUtils.vibrateDouble() // Double buzz
                soundUtils.playListeningStart() // Listening sound
                
                listeningView.visibility = android.view.View.VISIBLE
                speechUtils.startListening()
            }
        })
    }

    // Critical Method: Intercept touch events before they reach views
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (ev != null) {
            gestureDetector.onTouchEvent(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun handleVoiceCommand(command: String) {
        // Remove spaces for easier matching
        val normalized = command.replace(" ", "")
        android.util.Log.d("MainActivity", "Recognized command: $command (Normalized: $normalized)")
        
        when {
            // Enhanced mapping for Navigation with common misspellings/short forms
            normalized.contains("네비") || normalized.contains("내비") || normalized.contains("길안내") -> {
                speak("목적지를 말씀해주세요.")
                navigateToFragment(NavigationFragment.newInstance("서울역")) // Defaulting for demo
            }
            // Enhanced mapping for Walking
            normalized.contains("보행") || normalized.contains("걷기") || normalized.contains("산책") -> {
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
            tts.language = Locale.KOREA
            speakInitialGuide()
        }
    }
    
    private fun speakInitialGuide() {
        // Delay slightly to ensure TTS is ready and app is visible
        android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
            speak("블라인드 네비 앱입니다. 화면을 길게 누르면 음성 명령을 시작할 수 있습니다. 네비게이션 또는 보행이라고 말해보세요.")
        }, 1000)
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
        soundUtils.release()
        super.onDestroy()
    }
}
