package com.example.recorder.domain

import com.example.recorder.model.RecordDevice
import com.example.recorder.model.UdpPacket

interface RecorderUseCase {

    suspend fun start()
    suspend fun stop()
    suspend fun configurationDidChange()

    fun findHardwareDevices(): List<RecordDevice>

    var packetCatchHandler: ((UdpPacket) -> Unit)?

}