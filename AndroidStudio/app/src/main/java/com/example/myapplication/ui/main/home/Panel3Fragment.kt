package com.example.myapplication.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentFileBinding
import com.example.myapplication.databinding.FragmentPanel3Binding
import com.example.myapplication.databinding.FragmentPanelBinding

class Panel3Fragment() : Fragment() {

    lateinit var binding: FragmentPanel3Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPanel3Binding.inflate(inflater, container, false)

        return binding.root
    }
}