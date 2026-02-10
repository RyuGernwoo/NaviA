package com.example.blindnav.utils

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

class SpeechUtils(private val context: Context, private val onResult: (String) -> Unit, private val onError: (Int) -> Unit) {

    private var speechRecognizer: SpeechRecognizer? = null
    private val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")
        putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ko-KR")
        putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, "ko-KR")
        putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
    }

    init {
        initializeRecognizer()
    }

    private fun initializeRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {
                    Log.d("SpeechUtils", "onReadyForSpeech")
                }
                override fun onBeginningOfSpeech() {
                    Log.d("SpeechUtils", "onBeginningOfSpeech")
                }
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {
                    Log.d("SpeechUtils", "onEndOfSpeech")
                }
                
                override fun onError(error: Int) {
                    val errorMessage = when (error) {
                        SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                        SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                        SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                        SpeechRecognizer.ERROR_NETWORK -> "Network error"
                        SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                        SpeechRecognizer.ERROR_NO_MATCH -> "No match"
                        SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
                        SpeechRecognizer.ERROR_SERVER -> "Error from server"
                        SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
                        else -> "Unknown error"
                    }
                    Log.e("SpeechUtils", "Error: $error - $errorMessage")
                    this@SpeechUtils.onError(error) // Explicitly call constructor property to avoid recursion
                }

                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    Log.d("SpeechUtils", "onResults: $matches")
                    if (!matches.isNullOrEmpty()) {
                        onResult(matches[0])
                    } else {
                        this@SpeechUtils.onError(SpeechRecognizer.ERROR_NO_MATCH) // Consistent explicit call
                    }
                }

                override fun onPartialResults(partialResults: Bundle?) {
                    val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    Log.d("SpeechUtils", "onPartialResults: $matches")
                }
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })
        } else {
            Log.e("SpeechUtils", "Speech Recognition not available")
        }
    }

    fun startListening() {
        Log.d("SpeechUtils", "startListening called")
        try {
            speechRecognizer?.cancel()
            speechRecognizer?.startListening(speechIntent)
        } catch (e: Exception) {
            Log.e("SpeechUtils", "Exception details: ${e.message}")
            onError(SpeechRecognizer.ERROR_CLIENT)
        }
    }

    fun stopListening() {
        Log.d("SpeechUtils", "stopListening called")
        speechRecognizer?.stopListening()
    }

    fun destroy() {
        Log.d("SpeechUtils", "destroy called")
        speechRecognizer?.destroy()
    }
}
