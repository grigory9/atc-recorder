package com.example.recorder.domain

import com.example.recorder.model.UdpPacket

interface PacketsSendUseCase {

    fun sendPackets(packets: List<UdpPacket>)

    fun configurationDidChange()

}