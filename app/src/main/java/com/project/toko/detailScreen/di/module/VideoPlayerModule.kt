package com.project.toko.detailScreen.di.module

//import android.app.Application
//import androidx.media3.common.Player
//import androidx.media3.exoplayer.ExoPlayer
//import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.exoplayer.metaDataReader.MetaDataReader
//import com.project.toko.detailScreen.presentation_layer.detailScreen.mainPage.custom.exoplayer.metaDataReader.MetaDataReaderImpl
//import dagger.Module
//import dagger.Provides
//import javax.inject.Inject


//@Module
//class VideoPlayerModule @Inject constructor(private val application: Application) {
//
//    @Provides
//    fun provideVideoPlayer(): Player {
//        return ExoPlayer.Builder(application).build()
//    }
//    @Provides
//    fun provideMetaDataReader(app: Application): MetaDataReader {
//        return MetaDataReaderImpl(app)
//    }
//}