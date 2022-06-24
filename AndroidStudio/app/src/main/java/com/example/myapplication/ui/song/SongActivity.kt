package com.example.myapplication.ui.song

import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.example.myapplication.data.entities.Song
import com.example.myapplication.data.local.SongDatabase
import com.example.myapplication.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN // statusbar 숨기기

        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()
        initClickListener()

    }

    //사용자가 포커스를 잃었을 때 음악이 중지
    override fun onPause() {
        super.onPause()

        songs[nowPos].second = ((binding.musicprogressBar.progress * songs[nowPos].playTime)/100)/1000
        songs[nowPos].isPlaying = false
        setPlayerStatus(false)

        val sharedPreferences = getSharedPreferences("song", MODE_PRIVATE) // 내부저장소에 데이터를 저장할 수 있도록 해줌. 앱이 종료되었다가 실행되어도 저장된 데이터를 꺼내서 사용할 수 있음.
        val editor = sharedPreferences.edit() // 에디터

        editor.putInt("songId", songs[nowPos].id)

        editor.apply() //이 과정까지 해줘야 실제로 저장공간에 저장됨.
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()
        mediaPlayer?.release() // 미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제
    }

    private fun initPlayList() {
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    private fun initClickListener(){
        binding.songDownIb.setOnClickListener {
            finish()
        }
        binding.songPlayIv.setOnClickListener {
            setPlayerStatus(true)
        }

        binding.songPauseIv.setOnClickListener {
            setPlayerStatus(false)
        }

        binding.playerNext.setOnClickListener {
            moveSong(+1)
        }

        binding.playerPrevious.setOnClickListener {
            moveSong(-1)
        }

        binding.btnRepeatInactive.setOnClickListener {
            binding.btnRepeatInactive.visibility = View.GONE
            binding.btnRepeatAll.visibility = View.VISIBLE
        }

        binding.btnRepeatAll.setOnClickListener {
            binding.btnRepeatAll.visibility = View.GONE
            binding.btnRepeatOne.visibility = View.VISIBLE
        }

        binding.btnRepeatOne.setOnClickListener {
            binding.btnRepeatOne.visibility = View.GONE
            binding.btnRepeatInactive.visibility = View.VISIBLE
        }

        binding.btnRandomInactive.setOnClickListener {
            binding.btnRandomInactive.visibility = View.GONE
            binding.btnRandomActive.visibility = View.VISIBLE
        }

        binding.btnRandomActive.setOnClickListener {
            binding.btnRandomInactive.visibility = View.VISIBLE
            binding.btnRandomActive.visibility = View.GONE
        }

        binding.btnLike.setOnClickListener {
            setLike(songs[nowPos].isLike)
        }
    }

    private fun initSong() {
        val spf = getSharedPreferences("song", MODE_PRIVATE)
        val songId = spf.getInt("songId", 0)

        nowPos = getPlayingSongPosition(songId)

        Log.d("now Song ID", songs[nowPos].id.toString())

        startTimer()
        setPlayer(songs[nowPos])
    }

    private fun setLike(isLike: Boolean) {
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if(!isLike){
            binding.btnLike.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.btnUnlike.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    private fun moveSong(direct: Int) {
        if (nowPos + direct < 0) {
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if (nowPos + direct >= songs.size){
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        timer.interrupt()
        startTimer()

        mediaPlayer?.release() // 미디어플레이어가 갖고 있던 리소스 해제
        mediaPlayer = null // 미디어 플레이어 해제

        setPlayer(songs[nowPos])
    }

    private fun getPlayingSongPosition(songId: Int): Int{
        for (i in 0 until songs.size){
            if(songs[i].id == songId){
                return i
            }
        }
        return 0
    }

    private fun setPlayer(song: Song){
        binding.albumTitle.text = song.title
        binding.singer.text = song.singer
        binding.starttime.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.endtime.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.albumCoverIv.setImageResource((song.coverImg)!!)
        binding.musicprogressBar.progress = (song.second * 1000 / song.playTime)

        val music = resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if(song.isLike){
            binding.btnLike.setImageResource(R.drawable.ic_my_like_on)
        }else{
            binding.btnLike.setImageResource(R.drawable.ic_my_like_off)
        }

        setPlayerStatus(song.isPlaying)
    }

    private fun setPlayerStatus(isPlaying: Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying) {
            binding.songPlayIv.visibility = View.GONE
            binding.songPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
        }
        else {
            binding.songPlayIv.visibility = View.VISIBLE
            binding.songPauseIv.visibility = View.GONE
            if( mediaPlayer?.isPlaying == true)
                mediaPlayer?.pause()
        }
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
    }

    inner class Timer(private val playTime: Int, var isPlaying: Boolean = true): Thread() {
        private var second: Int = 0
        private var mills: Float = 0f

        override fun run() {
            super.run()
            try{
                while (true) {
                    if (second >= playTime){
                        break
                    }

                    if (isPlaying){
                        sleep(100)
                        mills += 100

                        runOnUiThread{
                            binding.musicprogressBar.progress = (mills / playTime * 100).toInt()
                        }

                        if (mills % 1000 == 0f){
                            runOnUiThread() {
                                binding.starttime.text = String.format("%02d:%02d", second / 60, second % 60)
                                second++
                            }
                        }
                    }
                }
            }catch (e: InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다.${e.message}")
            }

        }
    }
}