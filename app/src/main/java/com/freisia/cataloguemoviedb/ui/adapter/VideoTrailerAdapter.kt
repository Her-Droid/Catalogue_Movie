package com.freisia.cataloguemoviedb.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.freisia.cataloguemoviedb.BuildConfig
import com.freisia.cataloguemoviedb.R
import com.freisia.cataloguemoviedb.databinding.ItemVideoTrailerBinding
import com.freisia.cataloguemoviedb.domain.model.VideoTrailer
import com.freisia.cataloguemoviedb.utils.hide
import com.freisia.cataloguemoviedb.utils.show
import com.google.android.exoplayer2.util.Log
import com.yausername.youtubedl_android.YoutubeDL
import com.yausername.youtubedl_android.YoutubeDLRequest
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers

class VideoTrailerAdapter
    : PagingDataAdapter<VideoTrailer,VideoTrailerAdapter.VideoViewHolder>(VIDEO_COMPARATOR) {

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val binding = ItemVideoTrailerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideoViewHolder(binding)
    }


    class VideoViewHolder(private val itemVideoTrailerBinding: ItemVideoTrailerBinding):
       RecyclerView.ViewHolder(itemVideoTrailerBinding.root){

        private val parent: View = itemVideoTrailerBinding.root
        private val title = itemVideoTrailerBinding.videoTitle
        private val videoView = itemVideoTrailerBinding.autoPlayer
        private val imageView = itemVideoTrailerBinding.videoThumbnail
        private val volumeView = itemVideoTrailerBinding.videoVolume
        private val loadingView = itemVideoTrailerBinding.loading
        private var mute = true
        private val compositeDisposable = CompositeDisposable()

        fun bind(trailer: VideoTrailer){
            videoView.isMute = mute
            videoView.isLoading = true
            loadingView.show()
            volumeView.hide()
            val disposable = Observable.fromCallable {
                val request: YoutubeDLRequest
                if(trailer.site == "YouTube") {
                    request = YoutubeDLRequest(BuildConfig.YOUTUBE_URL + trailer.key)
                    request.addOption("-f", "best[height<=480] / wv*+ba/w")
                }
                else{
                    request = YoutubeDLRequest(BuildConfig.VIMEO_URL + trailer.key)
                    request.addOption("-f", "best[height<=480] / wv*+ba/w")
                }
                val uri = YoutubeDL.getInstance().getInfo(request)
                Log.i("VideoTrailerAdapter",uri.url)
                videoView.url = uri.url
                videoView.animationTime = 500
                videoView.placeholderView = imageView
            }.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    loadingView.hide()
                    volumeView.show()
                    videoView.isMute = mute
                    videoView.isLoading = false
                }){
                    loadingView.hide()
                    videoView.isLoading = false
                    videoView.isMute = mute
                    if(trailer.site == "YouTube") {
                        Toast.makeText(
                            parent.context,
                            "Streaming failed. failed to get stream info from ${BuildConfig.YOUTUBE_URL + trailer.key}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    else {
                        Toast.makeText(
                            parent.context,
                            "Streaming failed. failed to get stream info from ${BuildConfig.VIMEO_URL + trailer.key}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                }
            Log.i("VideoTrailerAdapter",BuildConfig.YOUTUBE_URL + trailer.key)
            volumeView.setOnClickListener {
                videoView.isMute = !mute
                mute = videoView.isMute
                if(videoView.isMute) volumeView.setImageResource(R.drawable.ic_volume_off)
                else volumeView.setImageResource(R.drawable.ic_volume_on)
            }
            if(videoView.isMute) volumeView.setImageResource(R.drawable.ic_volume_off)
            else volumeView.setImageResource(R.drawable.ic_volume_on)
            title.text = parent.resources.getString(R.string.title_text,trailer.type,trailer.name)
            Glide.with(itemVideoTrailerBinding.container.context)
                .load(
                    when (trailer.site) {
                        "YouTube" -> BuildConfig.YOUTUBE_IMAGE_URL + trailer.key + "/mqdefault.jpg"
                        "Vimeo" -> BuildConfig.VIMEO_IMAGE_URL + trailer.key + ".jpg"
                        else -> R.drawable.cover_placeholder
                    }
                )
                .error(R.drawable.cover_placeholder)
                .into(imageView)
            compositeDisposable.add(disposable)
        }
    }

    companion object{
        private val VIDEO_COMPARATOR = object : DiffUtil.ItemCallback<VideoTrailer>(){
            override fun areItemsTheSame(oldItem: VideoTrailer, newItem: VideoTrailer): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: VideoTrailer, newItem: VideoTrailer): Boolean {
                return oldItem == newItem
            }
        }
    }


}