package com.example.blindnav.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.blindnav.MainActivity
import com.example.blindnav.R

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.speak("안녕하세요, 나비아 입니다. 화면을 길게 눌러 음성으로 지시하세요.")
    }
}
