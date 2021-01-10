package com.example.recorder.repository

import com.example.recorder.model.UdpRecordAddress
import org.springframework.data.repository.Repository

interface UdpRecordAddressRepository: Repository<UdpRecordAddress, Long> {

        fun save(udpRecordAddress: UdpRecordAddress): UdpRecordAddress

        fun findAll(): List<UdpRecordAddress>

        fun findById(udpRecordAddressId: Long): List<UdpRecordAddress>

        fun deleteById(udpRecordAddressId: Long)

}