package com.example.recorder.service

import com.example.recorder.model.RecordDevice
import com.example.recorder.model.UdpRecordAddress
import com.example.recorder.model.UdpRecordFolder

interface ConfigurationService {

    fun addUdpRecordAddress(udpRecordAddress: UdpRecordAddress): UdpRecordAddress

    fun patchUdpRecordAddress(udpRecordAddress: UdpRecordAddress): UdpRecordAddress

    fun deleteUdpRecordAddress(id: Long)

    fun findAllUdpRecordAddress(): List<UdpRecordAddress>

    fun setupDevice(device: RecordDevice)

    fun findAllDevices() : List<RecordDevice>

    fun changeUdpRecordFolder(udpRecordFolder: UdpRecordFolder): UdpRecordFolder

    fun findUdpRecordFolder(): UdpRecordFolder

}