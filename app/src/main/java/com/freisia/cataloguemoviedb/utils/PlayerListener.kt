package com.freisia.cataloguemoviedb.utils

import com.google.android.exoplayer2.PlaybackException

interface PlayerListener {
    fun onPlayerReady() {}
    fun onPlayerStart() {}
    fun onPlayerStop() {}
    fun onPlayerProgress(positionMs: Long) {}
    fun onPlayerError(error: PlaybackException?) {}
    fun onPlayerBuffering(isBuffering: Boolean) {}
    fun onPlayerToggleControllerVisible(isVisible: Boolean) {}
}
