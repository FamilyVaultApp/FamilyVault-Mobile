package com.github.familyvault.services

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob

class AudioPlayerService : IAudioPlayerService {

    private var playingJob: Job? = null

    private val audioPlayerScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun play(audioData: ByteArray, onCompletion: (() -> Unit)?) {
        TODO("Method not yet implemented")
    }

    override fun stop() {
        TODO("Method not yet implemented")
    }
}
