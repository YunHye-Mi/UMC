package com.example.myapplication.ui.main.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.ui.main.album.AlbumFragment
import com.example.myapplication.ui.main.album.AlbumRVAdapter
import com.example.myapplication.R
import com.example.myapplication.data.local.SongDatabase
import com.example.myapplication.data.entities.Album
import com.example.myapplication.databinding.FragmentHomeBinding
import com.example.myapplication.ui.main.MainActivity
import com.google.gson.Gson

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var panelAdapter: PanelVPAdapter
    lateinit var bannerAdapter: BannerVPAdapter

    private var albumDatas = ArrayList<Album>()

    val handler: Handler = Handler(Looper.getMainLooper())

    private lateinit var songDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

//        binding.homeAlbumImgIv1.setOnClickListener() {
//            (context as MainActivity).supportFragmentManager.beginTransaction()
//                .replace(R.id.main_frm, AlbumFragment()).commitAllowingStateLoss()
//        }

        //데이터 리스트 생성 더미 데이터
//        albumDatas.apply {
//            add(Album("Butter", "방탄소년단 (BTS)", R.drawable.img_album_exp))
//            add(Album("Lilac", "아이유 (IU)", R.drawable.img_album_exp2))
//            add(Album("Next Level", "에스파 (AESPA)", R.drawable.img_album_exp3))
//            add(Album("Boy with Luv", "방탄소년단 (BTS)", R.drawable.img_album_exp4))
//            add(Album("BBoom BBoom", "모모랜드 (MOMOLAND)", R.drawable.img_album_exp5))
//            add(Album("Weekend", "태연 (Tae Yeon)", R.drawable.img_album_exp6)
//        }
        songDB = SongDatabase.getInstance(requireContext())!!
        albumDatas.addAll(songDB.albumDao().getAlbums())

        //더미 데이터랑 Adapter 연결
        val albumRVAdapter = AlbumRVAdapter(albumDatas)

        //리사이클러뷰에 어댑터 연결
        binding.TodayReleaseAlbumRv.adapter = albumRVAdapter
        binding.TodayReleaseAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        albumRVAdapter.setMyItemClickListner(object: AlbumRVAdapter.MyItemClickListner {
            override fun onItemClick(album: Album) {
                changeAlbumFragment(album)
            }

            override fun onRemoveAlbum(position: Int){
                albumRVAdapter.removeItem(position)
            }
        })

        panelAdapter = PanelVPAdapter(this)

        panelAdapter.addFragment(PanelFragment())
        panelAdapter.addFragment(Panel1Fragment())
        panelAdapter.addFragment(Panel2Fragment())
        panelAdapter.addFragment(Panel3Fragment())
        panelAdapter.addFragment(Panel4Fragment())
        panelAdapter.addFragment(Panel5Fragment())
        panelAdapter.addFragment(Panel6Fragment())

        binding.homePanelBackgroudVp.adapter = panelAdapter

        binding.homePanelCi.setViewPager(binding.homePanelBackgroudVp)
        binding.homePanelCi.createIndicators(7, 0)

        binding.homePanelBackgroudVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                binding.homePanelCi.animatePageSelected(position)
            }
        })
        
        bannerAdapter = BannerVPAdapter(this)

        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        panel().start()

        return binding.root
    }

    private fun changeAlbumFragment(album: Album) {
        (context as MainActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    val gson = Gson()
                    val albumJson = gson.toJson(album)
                    putString("album", albumJson)
                }
            }).commitAllowingStateLoss()
    }

    override fun onDestroy() {
        super.onDestroy()
        panel().interrupt()
    }


    inner class panel(): Thread() {
        var currentPosition: Int = 0

        override fun run() {
            super.run()
            try {
                while (true){
                    Thread.sleep(5000)
                    currentPosition++
                    if (currentPosition == panelAdapter.itemCount) currentPosition = 0
                    handler.post {
                        binding.homePanelBackgroudVp.setCurrentItem(currentPosition, true)
                        true
                    }
                }
            }catch (e:InterruptedException) {
                Log.d("interrupt", "Home Fragment 스레드가 종료되었습니다.")
            }
        }
    }
}
