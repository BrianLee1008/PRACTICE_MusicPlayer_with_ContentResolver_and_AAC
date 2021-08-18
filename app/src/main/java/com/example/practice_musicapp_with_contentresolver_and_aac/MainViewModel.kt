package com.example.practice_musicapp_with_contentresolver_and_aac

import android.app.Application
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MainViewModel(application: Application) : AndroidViewModel(application) {
	private val context = getApplication<Application>().applicationContext

	private val _musicLiveData = MutableLiveData<Any>(getMusicList())
	val musicLiveData : LiveData<Any>
	get() = _musicLiveData

	fun getMusicList() : List<MusicData> {
		val musicList = mutableListOf<MusicData>()

		val musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
		val proJ = arrayOf(
			MediaStore.Audio.Media._ID,
			MediaStore.Audio.Media.TITLE,
			MediaStore.Audio.Media.ARTIST,
			MediaStore.Audio.Media.ALBUM_ID,
			MediaStore.Audio.Media.DURATION
		)

		val cursor = context.contentResolver.query(musicUri, proJ, null, null, null)

		while (cursor?.moveToNext() == true){
			val id = cursor.getString(0)
			val title = cursor.getString(1)
			val artist = cursor.getString(2)
			val albumId = cursor.getString(3)
			val duration = cursor.getLong(4)

			val music = MusicData(id, title, artist, albumId, duration)
			musicList.add(music)
		}
		return musicList
	}
}