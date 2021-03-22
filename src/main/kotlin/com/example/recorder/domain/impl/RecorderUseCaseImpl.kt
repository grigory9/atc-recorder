package com.example.recorder.domain.impl

import com.example.recorder.domain.RecorderUseCase
import com.example.recorder.model.RecordDevice
import com.example.recorder.repository.RecordDeviceRepository
import com.example.recorder.repository.UdpRecordAddressRepository
import com.example.recorder.model.UdpPacket
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import org.pcap4j.core.*
import org.pcap4j.packet.Packet
import org.springframework.stereotype.Component
import java.io.IOException
import java.util.*

@Component
class RecorderUseCaseImpl(
    val udpRecordAddressRepository: UdpRecordAddressRepository,
    val udpRecordDeviceRepository: RecordDeviceRepository
 ): RecorderUseCase {
    override var packetCatchHandler: ((UdpPacket) -> Unit)? = null

    private var handler: PcapHandle? = null
    private var dispatcher = newSingleThreadContext("Recorder thread")

    override suspend fun start() {
        var currentDeviceName = udpRecordDeviceRepository.findAll().findLast { it.isPrimary == true }
        var selectedDevice: PcapNetworkInterface? = try {
            Pcaps
                .findAllDevs()
                .find { (it.name == currentDeviceName?.deviceName) }
        } catch (e: PcapNativeException) {
            throw IOException(e.message)
        }

        if (handler == null ) {
            handler = selectedDevice?.openLive(65536,
                PcapNetworkInterface.PromiscuousMode.PROMISCUOUS,
                10)
        }

        setFilters()
        launchHandler()
    }

    override suspend fun stop(): Unit = coroutineScope {
        handler?.breakLoop()
        handler = null
    }

    override suspend fun configurationDidChange() = coroutineScope {
        if (handler != null) {
            stop()
            start()
        }
    }

    override fun findHardwareDevices(): List<RecordDevice> =
         Pcaps
            .findAllDevs()
            .filterNotNull()
            .map { RecordDevice(null, it.name) }

    fun setFilters() {
        val udpAddresses = udpRecordAddressRepository.findAll()
        val filter = udpAddresses.joinToString(separator = " or ") { "(dst ${it.listeningIp} and port ${it.listeningPort})" }
        try {
            handler?.setFilter(filter, BpfProgram.BpfCompileMode.OPTIMIZE)
        } catch (e: PcapNativeException) {
            throw IOException(e.message)
        }
    }

    suspend fun launchHandler() = coroutineScope {
        launch(dispatcher) {
            handler?.loop(
                -1,
            ) { packet: Packet ->
                val udpPacket = UdpPacket.create(packet, Date())
                packetCatchHandler?.let { it(udpPacket) }
            }
        }
    }
}