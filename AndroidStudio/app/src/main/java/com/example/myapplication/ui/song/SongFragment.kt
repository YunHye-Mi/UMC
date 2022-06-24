package com.example.myapplication.ui.song

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myapplication.databinding.FragmentSongBinding

class SongFragment: Fragment() {

    lateinit var binding: FragmentSongBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSongBinding.inflate(inflater, container, false)

                binding.btnToggleOff.setOnClickListener() {
            binding.btnToggleOff.visibility = View.GONE
            binding.btnToggleOn.visibility = View.VISIBLE
        }

        binding.btnToggleOn.setOnClickListener() {
            binding.btnToggleOff.visibility = View.VISIBLE
            binding.btnToggleOn.visibility = View.GONE
        }

        binding.songLilacLayout.setOnClickListener() {
            Toast.makeText(activity, "LILAC", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }
}