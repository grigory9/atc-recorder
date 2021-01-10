package com.example.recorder.model

import java.sql.Timestamp
import java.util.*

data class UdpPacket(
    val byteArray: ByteArray,
    val date: Date
) {
    val length: Int by lazy { byteArray.copyOfRange(6, 8).getUShort().toInt() + 4 }
    val sourceIpAddress: String by lazy { convertBytesToIpString(byteArray.copyOfRange(16, 20)) }
    val destinationIpAddress: String by lazy { convertBytesToIpString(byteArray.copyOfRange(20, 24)) }
    val sourcePort: Int by lazy { byteArray.copyOfRange(24, 26).getUShort().toInt() }
    val destinationPort: Int by lazy { byteArray.copyOfRange(26, 28).getUShort().toInt() }
    val contentBytes: ByteArray by lazy { byteArray.copyOfRange(32, length) }
    val content: String by lazy { contentBytes.toString(Charsets.UTF_8) }

    init {
//        var family = byteArray.copyOfRange(0, 4)
//        var headerLength = byteArray.copyOfRange(4, 5)
//        var explicitCongestionNotification = byteArray.copyOfRange(5, 6)
//        var identification = byteArray.copyOfRange(8, 10)
//        var fragmentOffset = byteArray.copyOfRange(10, 12)
//        var timeToLive = byteArray.copyOfRange(12, 13)
//        var protocol = byteArray.copyOfRange(13, 14)
//        var headerChecksum = byteArray.copyOfRange(14, 16)
//        var length = byteArray.copyOfRange(28, 30)
//        var checksum = byteArray.copyOfRange(30, 32)
    }

    private fun convertBytesToIpString(bytes: ByteArray): String =
        bytes.joinToString(".") { it.toString() }

    fun convertToString(): String {
//        val byteStr = packet.last().rawData.toString(Charsets.UTF_8)
        val timestamp = Timestamp(date.getTime())
        return "${timestamp.time.toString()} $content"
    }
}

fun ByteArray.getUShort() =
    ((this[0].toUInt() and 0xFFu) shl 8) or
            ((this[1].toUInt() and 0xFFu) shl 0)