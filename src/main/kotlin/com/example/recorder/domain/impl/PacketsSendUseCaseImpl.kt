package com.example.recorder.domain.impl

import com.example.recorder.domain.PacketsSendUseCase
import com.example.recorder.model.UdpPacket
import com.example.recorder.model.UdpRecordAddress
import com.example.recorder.repository.UdpRecordAddressRepository
import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import io.ktor.util.network.*
import io.ktor.utils.io.errors.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.pcap4j.packet.namednumber.UdpPort
import org.springframework.stereotype.Component
import java.lang.Error
import java.net.*

@Component
class PacketsSendUseCaseImpl(
    val udpRecordAddressRepository: UdpRecordAddressRepository
): PacketsSendUseCase {
    var udpRecordAddressList: List<UdpRecordAddress>? = null

    init {
        updateConfiguration()
    }

    override fun configurationDidChange() {
        updateConfiguration()
    }

    override fun sendPackets(packets: List<UdpPacket>) {
        GlobalScope.async {
            packets.forEach {
                send(it)
            }
        }
    }

    private fun send(packet: UdpPacket) {
        try {
            val socket = DatagramSocket()
            //Open a port to send the package
            val sendData = packet.contentBytes
            val address = sendAddressForUdpPacket(packet)
            val sendPacket = DatagramPacket(sendData, sendData.size, address)
            println("${sendPacket.address.hostName}," +
                    " ${sendPacket.address.hostAddress}," +
                    " ${sendPacket.socketAddress.hostname}," +
                    " ${sendPacket.port} ")
            socket.send(sendPacket)
        } catch (e: IOException) {
            println("IOException: " + e.message)
        }
    }

    private fun sendAddressForUdpPacket(packet: UdpPacket): InetSocketAddress {
        val address = udpRecordAddressList?.filter {
            (it.listeningIp == packet.destinationIpAddress) &&
            (it.listeningPort == packet.destinationPort)
        }

        if (address?.count() != 1) {
            throw Error("Expect appropriate address count 1, instead ${address?.count()}")
        }

        val destinationAddress = address.last()
        return InetSocketAddress(destinationAddress.destinationIp,
                                 destinationAddress.destinationPort)
    }

    private fun updateConfiguration() {
        udpRecordAddressList = udpRecordAddressRepository.findAll()
    }
}