package com.example.recorder.domain

import com.example.recorder.model.PlaybackStartOptions
import com.example.recorder.model.RawPacket
import com.example.recorder.model.UdpPacket
import java.util.*

interface PcapDumpUseCase {

    fun getDump(fromDate: Date, toDate: Date)

    var readPacketDumpCallback: ((Date, Date) -> List<RawPacket>)?
}