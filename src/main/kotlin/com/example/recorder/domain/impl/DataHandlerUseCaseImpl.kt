package com.example.recorder.domain.impl

import com.example.recorder.domain.DataHandlerUseCase
import com.example.recorder.model.RawPacket
import com.example.recorder.model.UdpPacket
import com.example.recorder.repository.UdpRecordFolderRepository
import com.example.recorder.utils.extensions.hexStringToByteArray
import com.example.recorder.utils.extensions.toHex
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
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

    override fun readDump(fromDate: Date, toDate: Date): List<RawPacket> {
        var packets = mutableListOf<RawPacket>()
        var newFromDate = Date(fromDate.time)

        while (newFromDate.before(toDate)) {

            val batch = readRawBatch(newFromDate)
            packets.addAll(batch)

            newFromDate = Date(newFromDate.time + millisecondsStep)
        }

        return packets
    }

    override fun readBatch(fromDate: Date): List<UdpPacket> =
        readRawBatch(fromDate).map {
            UdpPacket.create(it.byteArray, it.timestamp)
        }

    private fun readRawBatch(fromDate: Date): List<RawPacket> {
        val rootFolder = udpRecordFolderRepository.find().folder
        val fileName = makeFileName(fromDate)
        var packets = mutableListOf<RawPacket>()
        File(rootFolder).walkTopDown().forEach {
            if (it.name == fileName) {
                val newPackets = decodeUdpFile(it)
                packets.addAll(newPackets)
            }
        }
        return packets
    }

    private fun decodeUdpFile(file: File): List<RawPacket> =
        file
            .bufferedReader()
            .readLines()
            .map {
                val bytesAndDate = it.split(" ")
                val bytes = bytesAndDate[0].hexStringToByteArray()
                val date = Date(bytesAndDate[1].toLong())
                RawPacket(bytes, date)
            }

    private fun makeFileName(date: Date): String {
        val time = date.time - (date.time % millisecondsStep)
        val formatter = SimpleDateFormat("yyyy-MM-dd:HH:mm")
        return formatter.format(Date(time))
    }

    private fun makePath(udpPacket: UdpPacket): String =
        "${udpRecordFolderRepository.find().folder}/${udpPacket.destinationIpAddress}:${udpPacket.destinationPort}"
}

