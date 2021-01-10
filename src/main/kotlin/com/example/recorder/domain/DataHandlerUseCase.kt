package com.example.recorder.domain

import com.example.recorder.model.UdpPacket
import java.util.*

interface DataHandlerUseCase {

    fun writeUdp(udpPacket: UdpPacket)

    fun readBatch(fromDate: Date): List<UdpPacket>

}