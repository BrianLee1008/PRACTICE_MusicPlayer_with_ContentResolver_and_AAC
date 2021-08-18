package com.example.practice_musicapp_with_contentresolver_and_aac

import android.media.MediaPlayer
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.practice_musicapp_with_contentresolver_and_aac.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

class MusicAdapter(private val listener: OnItemClickListener) :
	ListAdapter<MusicData, MusicAdapter.MusicViewHolder>(
		object : DiffUtil.ItemCallback<MusicData>() {
			override fun areItemsTheSame(oldItem: MusicData, newItem: MusicData): Boolean = false


			override fun areContentsTheSame(oldItem: MusicData, newItem: MusicData): Boolean = false

		}
	) {
	var mediaPlayer: MediaPlayer? = null

	interface OnItemClickListener {
		fun setClickListener(position: Int)
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicViewHolder =
		MusicViewHolder(
			ItemRecyclerBinding.inflate(
				LayoutInflater.from(parent.context), parent, false
			), listener
		)

	override fun onBindViewHolder(holder: MusicViewHolder, position: Int) {
		holder.updateView(getItem(position))
	}

	inner class MusicViewHolder(
		private val binding: ItemRecyclerBinding,
		listener: OnItemClickListener
	) :
		RecyclerView.ViewHolder(binding.root) {
		var musicUri: Uri? = null

		init {
			itemView.setOnClickListener(){
				if (adapterPosition != RecyclerView.NO_POSITION || mediaPlayer != null) {
					mediaPlayer?.release() // 공개, 방출
					mediaPlayer = null

					listener.setClickListener(adapterPosition)
				}
				mediaPlayer = MediaPlayer.create(itemView.context, musicUri)
				mediaPlayer?.start()
			}

		}


		fun updateView(music: MusicData) {
			binding.run {
				textTitle.text = music.title
				textArtist.text = music.artist
				val duration = SimpleDateFormat("mm:ss").format(music.duration)
				textDuration.text = duration

				imageAlbum.setImageURI(music.getAlbumUri())
			}
			musicUri = music.getMusicUri()
		}
	}


}
