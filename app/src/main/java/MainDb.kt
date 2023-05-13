//package com.example.animeapp.dao
//
//import android.content.Context
//import androidx.room.Database
//import androidx.room.Room
//import androidx.room.RoomDatabase
//
//@Database(entities = [AnimeItem::class], version = 1)
//abstract class MainDb : RoomDatabase() {
//
//    abstract fun getDao(): Dao
//
//    companion object {
//        private var INSTANCE: MainDb? = null
//
//        @JvmStatic
//        fun getDb(context: Context): MainDb {
//            synchronized(this) {
//                if (INSTANCE == null) {
//                    INSTANCE = Room.databaseBuilder(
//                        context.applicationContext,
//                        MainDb::class.java,
//                        "toko.db"
//                    ).build()
//                }
//                return INSTANCE as MainDb
//            }
//        }
//    }
//}
