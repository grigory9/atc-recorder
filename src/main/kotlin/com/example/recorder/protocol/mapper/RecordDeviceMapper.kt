package com.example.recorder.protocol.mapper

import com.example.recorder.model.UdpRecordAddress
import com.example.recorder.protocol.UdpRecordAddressDto
import org.springframework.stereotype.Component

@Component
class UdpRecordAddressMapper {

    fun map(dto: UdpRecordAddressDto) = UdpRecordAddress(
        listeningIp = dto.listeningIp,
        listeningPort = dto.listeningPort,
        destinationIp = dto.destinationIp,
        destinationPort = dto.destinationPort
    )

    fun map(model: UdpRecordAddress) = UdpRecordAddressDto(
        id = model.id!!,
        listeningPort = model.listeningPort,
        listeningIp = model.listeningIp,
        destinationIp = model.destinationIp,
        destinationPort = model.destinationPort
    )

}
