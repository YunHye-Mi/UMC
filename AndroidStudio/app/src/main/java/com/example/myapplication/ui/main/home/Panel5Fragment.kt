package com.example.myapplication.ui.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentFileBinding
import com.example.myapplication.databinding.FragmentPanel5Binding
import com.example.myapplication.databinding.FragmentPanelBinding

class Panel5Fragment() : Fragment() {

    lateinit var binding: FragmentPanel5Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPanel5Binding.inflate(inflater, container, false)


        return binding.root
    }
}