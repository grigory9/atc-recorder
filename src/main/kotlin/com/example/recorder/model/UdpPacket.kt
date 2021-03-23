package com.example.recorder.model

import org.pcap4j.packet.*
import java.lang.Exception
import java.util.*
import org.pcap4j.packet.UdpPacket as pcapUdpPacket

data class UdpPacket(
    val sourceIpAddress: String,
    val destinationIpAddress: String,
    val sourcePort: Int,
    val destinationPort: Int,
    val byteArray: ByteArray,
    val contentBytes: ByteArray,
    val date: Date,
    var pcapPacket: Packet
) {
    companion object Builder {
        fun create(byteArray: ByteArray, date: Date): UdpPacket {
            val ethernetPacket = EthernetPacket.newPacket(byteArray, 0, byteArray.size)
            if(ethernetPacket.payload is IpV4Packet) {
                return create(ethernetPacket, date)
            }

            val bsdLoopPacket = BsdLoopbackPacket.newPacket(byteArray, 0, byteArray.size)
            if(bsdLoopPacket.payload is IpV4Packet) {
                return create(bsdLoopPacket, date)
            }

            throw Exception("Cannot define packet type")
        }

        fun create(packet: Packet, date: Date): UdpPacket {
            val ipv4 = packet.payload
            if(ipv4 is IpV4Packet) {
                val udp = ipv4.payload
                if(udp is pcapUdpPacket) {
                    return UdpPacket(ipv4.header.srcAddr.hostAddress,
                        ipv4.header.dstAddr.hostAddress,
                        udp.header.srcPort.valueAsInt(),
                        udp.header.dstPort.valueAsInt(),
                        packet.rawData,
                        udp.payload.rawData,
                        date,
                        packet
                    )
                } else {
                    throw Exception("Cannot map payload to UdpPacket")
                }
            } else {
                throw Exception("Cannot map packet to IpV4Packet")
            }
        }
    }
}

fun ByteArray.getUShort() =
    ((this[0].toUInt() and 0xFFu) shl 8) or
            ((this[1].toUInt() and 0xFFu) shl 0)