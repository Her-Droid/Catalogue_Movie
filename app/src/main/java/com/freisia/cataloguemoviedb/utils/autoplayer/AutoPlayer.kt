package com.freisia.cataloguemoviedb.utils.autoplayer

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.freisia.cataloguemoviedb.utils.PlayerListener
import com.google.android.exoplayer2.ui.StyledPlayerView

class AutoPlayer : FrameLayout {
    var animationTime: Long = 1000
    var url: String? = ""
    var placeholderView: View? = null
    var playerListener: PlayerListener? = null

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    var isMute: Boolean = true
        set(value) {
            field = value
            if (playerView != null && playerView!!.tag != null && playerView!!.tag is AutoPlayerManager) {
                val autoPlayExoPlayerHelper = (playerView!!.tag as AutoPlayerManager)
                autoPlayExoPlayerHelper.isMute = value
                when {
                    value -> autoPlayExoPlayerHelper.helperForExoPlayer?.mute()
                    else -> autoPlayExoPlayerHelper.helperForExoPlayer?.unMute()
                }
            }
        }

    var isLoading = true
        set(value){
            field = value
            if (playerView != null && playerView!!.tag != null && playerView!!.tag is AutoPlayerManager && url?.isNotBlank() == true) {
                val autoPlayExoPlayerHelper = (playerView!!.tag as AutoPlayerManager)
                when {
                    value -> autoPlayExoPlayerHelper.helperForExoPlayer?.pause()
                    else -> autoPlayExoPlayerHelper.helperForExoPlayer?.setUrl(url!!,true)
                }
            }
        }


    var playerView: StyledPlayerView? = null

    fun addPlayer(playerView: StyledPlayerView) {
        if (this.playerView == null) {
            this.playerView = playerView
            addView(playerView)
        }
    }

    fun removePlayer() {
        if (playerView != null) {
            removeView(playerView)
            playerView = null
            placeholderView?.visibility = View.VISIBLE
            placeholderView
                ?.animate()
                ?.setDuration(animationTime)
                ?.alpha(1f)
            playerListener?.onPlayerStop()
        }
    }

    override fun removeView(view: View?) {
        super.removeView(view)
        if (view is StyledPlayerView) {
            playerView = null
            placeholderView?.visibility = View.VISIBLE

            placeholderView
                ?.animate()
                ?.setDuration(animationTime)
                ?.alpha(1f)
        }
    }

    fun hidePlaceholderView(thumbHideDelay: Long) {
        placeholderView
            ?.animate()
            ?.setStartDelay(thumbHideDelay)
            ?.setDuration(animationTime)
            ?.alpha(0f)
    }
}
