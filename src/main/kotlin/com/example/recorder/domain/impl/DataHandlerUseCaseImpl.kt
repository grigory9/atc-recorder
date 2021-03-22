package com.example.recorder.domain.impl

import com.example.recorder.domain.DataHandlerUseCase
import com.example.recorder.model.UdpPacket
import com.example.recorder.repository.UdpRecordFolderRepository
import com.example.recorder.utils.extensions.hexStringToByteArray
import com.example.recorder.utils.extensions.toHex
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.io.use

@Component
class DataHandlerUseCaseImpl(
    val udpRecordFolderRepository: UdpRecordFolderRepository
): DataHandlerUseCase {

    private val millisecondsStep: Int = 300000 // 300 sec, 5 min

    override fun writeUdp(udpPacket: UdpPacket) {
        val path = makePath(udpPacket)
        val fileName = makeFileName(udpPacket.date)

        val file = File("$path/$fileName")
        if (!file.exists()) {
            val pathDir = File(path)
            pathDir.mkdirs()
            file.createNewFile()
        }

        FileOutputStream(file, true).bufferedWriter().use {
            it.appendLine("${udpPacket.byteArray.toHex()} ${udpPacket.date.time}")
        }
    }

    override fun readBatch(fromDate: Date): List<UdpPacket> {
        val rootFolder = udpRecordFolderRepository.find().folder
        val fileName = makeFileName(fromDate)
        var packets = mutableListOf<UdpPacket>()
        File(rootFolder).walkTopDown().forEach {
            if (it.name == fileName) {
                val newPackets = decodeUdpFile(it)
                packets.addAll(newPackets)
            }
        }
        return packets
    }

    private fun decodeUdpFile(file: File): List<UdpPacket> =
        file
            .bufferedReader()
            .readLines()
            .map {
                val bytesAndDate = it.split(" ")
                val bytes = bytesAndDate[0].hexStringToByteArray()
                val date = Date(bytesAndDate[1].toLong())
                UdpPacket.create(bytes, date)
            }

    private fun makeFileName(date: Date): String {
        val time = date.time - (date.time % millisecondsStep)
        val formatter = SimpleDateFormat("yyyy-MM-dd:HH:mm")
        return formatter.format(Date(time))
    }

    private fun makePath(udpPacket: UdpPacket): String =
        "${udpRecordFolderRepository.find().folder}/${udpPacket.destinationIpAddress}:${udpPacket.destinationPort}"
}

