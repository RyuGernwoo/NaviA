package com.example.blindnav.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.blindnav.MainActivity
import com.example.blindnav.R

class NavigationFragment : Fragment() {

    private var destination: String = "Unknown"

    companion object {
        fun newInstance(destination: String): NavigationFragment {
            val fragment = NavigationFragment()
            val args = Bundle()
            args.putString("dest", destination)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        destination = arguments?.getString("dest") ?: "Unknown"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_navigation, container, false)
        view.findViewById<TextView>(R.id.tvDestination).text = destination
        return view
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.speak("$destination 안내를 시작합니다. 남은 거리는 850미터입니다.")
    }
}
