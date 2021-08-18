package com.example.practice_musicapp_with_contentresolver_and_aac

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice_musicapp_with_contentresolver_and_aac.databinding.ActivityMainBinding

/*xo
*  1. 퍼미션으로 외부저장소 승인
* 	2. 리사이클러뷰 ListAdapter 만들어주고 아이템 만들어주고
* 	3. MusicData 클래스 만들고
* 	4. ViewModel에 음원목록 읽어오는 메서드 만들고 LiveData
* 	5. 어댑터만들고, mediaPlayer 만들어서 음원 누르면 재생 (interface)
* 	6. Activity에서 LiveData 옵저빙
* */

class MainActivity : BaseActivity(), MusicAdapter.OnItemClickListener {

	var musicAdapter = MusicAdapter(this)

	private lateinit var binding: ActivityMainBinding
	private val viewModel by lazy { ViewModelProvider(this).get(MainViewModel::class.java) }

	override fun permissionGranted(requestCode: Int) {
		when (requestCode) {
			100 -> {
				startProcess()
				initRecyclerView()
			}
		}
	}

	override fun permissionDenied(requestCode: Int) {
		Toast.makeText(this, "허용되지 않은 외부저장소 입니다.", Toast.LENGTH_SHORT).show()
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityMainBinding.inflate(layoutInflater)
		setContentView(binding.root)

		requirePermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
		setListener()

	}

	private fun startProcess() {
		viewModel.musicLiveData.observe(
			this, {
				(binding.recyclerView.adapter as MusicAdapter).submitList(viewModel.getMusicList())
			}
		)
		binding.recyclerView.adapter = musicAdapter
	}

	private fun initRecyclerView() {
		binding.recyclerView.let {
			it.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

		}
	}

	override fun setClickListener(position: Int) {
	}

	private fun setListener(){
		binding.button.setOnClickListener(){
			val mediaPlayer = musicAdapter.mediaPlayer
			mediaPlayer?.stop()
		}
	}

}