package com.example.myapplication.ui.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.data.entities.Album
import com.example.myapplication.data.local.SongDatabase
import com.example.myapplication.databinding.FragmentSavedalbumBinding

class SavedAlbumFragment: Fragment() {

    lateinit var binding: FragmentSavedalbumBinding
    lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedalbumBinding.inflate(inflater, container, false)

        songDB = SongDatabase.getInstance(requireContext())!!


        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview() {
        binding.saveAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val albumlockerRVAdapter = AlbumLockerRVAdapter()

        albumlockerRVAdapter.setMyItemClickListener(object :
            AlbumLockerRVAdapter.MyItemClickListener {
            override fun onRemoveSong(songId: Int) {
                songDB.albumDao().getLikedAlbums(getJwt())
            }
        })

        binding.saveAlbumRv.adapter = albumlockerRVAdapter

        albumlockerRVAdapter.addAlbums(songDB.albumDao().getLikedAlbums(getJwt()) as ArrayList<Album>)
    }

    private fun getJwt(): Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)
    }
}