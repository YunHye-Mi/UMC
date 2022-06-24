package com.example.myapplication.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentFileBinding
import com.example.myapplication.databinding.FragmentPanel6Binding
import com.example.myapplication.databinding.FragmentPanelBinding

class Panel6Fragment() : Fragment() {

    lateinit var binding: FragmentPanel6Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPanel6Binding.inflate(inflater, container, false)

        return binding.root
    }
}