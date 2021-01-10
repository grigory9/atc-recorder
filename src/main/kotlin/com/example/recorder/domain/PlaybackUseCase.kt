package com.example.recorder.domain

import com.example.recorder.model.PlaybackStartOptions
import com.example.recorder.model.UdpPacket
import java.util.*

interface PlaybackUseCase {

    suspend fun start(options: PlaybackStartOptions)

    suspend fun stop()

    var readPacketBatchCallback: ((Date) -> List<UdpPacket>)?
    var sendPacketsCallback: ((List<UdpPacket>) -> Unit)?
}