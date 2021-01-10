package com.example.recorder.service.impl

import com.example.recorder.domain.PacketsSendUseCase
import com.example.recorder.domain.PlaybackUseCase
import com.example.recorder.repository.RecordDeviceRepository
import com.example.recorder.domain.RecorderUseCase
import com.example.recorder.model.RecordDevice
import com.example.recorder.model.UdpRecordAddress
import com.example.recorder.model.UdpRecordFolder
import com.example.recorder.repository.UdpRecordAddressRepository
import com.example.recorder.repository.UdpRecordFolderRepository
import com.example.recorder.service.ConfigurationService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.stereotype.Service

@Service
class ConfigurationServiceImpl(
    val recorderUseCase: RecorderUseCase,
    val playbackUseCase: PlaybackUseCase,
    val packetsSendUseCase: PacketsSendUseCase,
    val udpRecordAddressRepository: UdpRecordAddressRepository,
    val recordDeviceRepository: RecordDeviceRepository,
    val udpRecordFolderRepository: UdpRecordFolderRepository
) : ConfigurationService {

    override fun addUdpRecordAddress(udpRecordAddress: UdpRecordAddress) {
        udpRecordAddressRepository.save(udpRecordAddress)

        GlobalScope.async {
            playbackUseCase.stop()
            recorderUseCase.configurationDidChange()
            packetsSendUseCase.configurationDidChange()
        }
    }

    override fun findAllUdpRecordAddress(): List<UdpRecordAddress> =
        udpRecordAddressRepository.findAll()

    override fun setupDevice(device: RecordDevice) {
        val devs = recordDeviceRepository.findAll()
        devs.forEach {
            recordDeviceRepository.deleteById(it.id!!)
        }
        recordDeviceRepository.save(device)

        GlobalScope.async {
            playbackUseCase.stop()
            recorderUseCase.configurationDidChange()
        }
    }

    override fun findAllDevices(): List<RecordDevice> {
        var allDevices = recorderUseCase.findHardwareDevices()
        val currentDevice = recordDeviceRepository.findAll().findLast { it.isPrimary }
        var dev = allDevices.find { it.deviceName == currentDevice?.deviceName ?: false }
        dev?.isPrimary = (currentDevice?.isPrimary == true)
        return allDevices
    }

    override fun changeUdpRecordFolder(udpRecordFolder: UdpRecordFolder): UdpRecordFolder {
        val newFolder = udpRecordFolderRepository.save(udpRecordFolder)
        GlobalScope.async {
            recorderUseCase.configurationDidChange()
        }
        return newFolder
    }

    override fun findUdpRecordFolder(): UdpRecordFolder =
        udpRecordFolderRepository.find()
}