package com.example.myapplication.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.myapplication.data.entities.Album
import com.example.myapplication.data.entities.Like
import com.example.myapplication.data.entities.Song
import com.example.myapplication.data.entities.User


@Database(entities = [Song::class, User::class, Like::class, Album::class], version = 1)
abstract class SongDatabase : RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun userDao(): UserDao
    abstract fun albumDao(): AlbumDao

    companion object{
        private var instance: SongDatabase? = null

        @Synchronized
        fun getInstance(context: Context): SongDatabase? {
            if(instance == null) {
                synchronized(SongDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SongDatabase::class.java,
                        "Song-database"//다른 데이터 베이스랑 이름겹치면 꼬임
                    ).allowMainThreadQueries().build()
                }
            }
            return instance
        }
    }
}