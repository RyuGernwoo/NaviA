package com.example.blindnav.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blindnav.MainActivity
import com.example.blindnav.R

class WalkingFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_walking, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.speak("보행 안전 모드입니다. 주변 위험을 감지합니다.")
    }
}
