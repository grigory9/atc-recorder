package com.example.recorder.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity(name = "udp_record_address")
data class UdpRecordAddress(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    val listeningIp: String,
    val listeningPort: Int,
    val destinationIp: String,
    val destinationPort: Int
)