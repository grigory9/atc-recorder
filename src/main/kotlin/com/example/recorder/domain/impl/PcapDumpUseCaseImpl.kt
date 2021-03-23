package com.example.recorder.domain.impl

import com.example.recorder.domain.PcapDumpUseCase
import com.example.recorder.model.RawPacket
import org.pcap4j.core.Pcaps
import org.pcap4j.packet.namednumber.DataLinkType
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileReader
import java.sql.Timestamp
import java.util.*

@Component
class PcapDumpUseCaseImpl: PcapDumpUseCase {
    override var readPacketDumpCallback: ((Date, Date) -> List<RawPacket>)? = null

    override fun getDump(fromDate: Date, toDate: Date): ByteArray {

        val handler = Pcaps.openDead(DataLinkType.RAW, 65535)
        val currentDate = Date().time
        val pathname = "/tmp/$currentDate"
        val pcapDump = handler.dumpOpen(pathname)

        readPacketDumpCallback?.let {
            it(fromDate, toDate)
        }?.forEach { packet -> Unit
            pcapDump.dumpRaw(packet.byteArray, Timestamp(packet.timestamp.time))
        }

        pcapDump.close()
        handler.close()

        return File("/tmp/$currentDate").readBytes()
    }
}