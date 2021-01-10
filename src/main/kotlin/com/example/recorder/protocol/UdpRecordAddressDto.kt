package com.example.recorder.protocol

import org.hibernate.validator.constraints.Range
import javax.validation.constraints.Pattern

const val ipRegexpValidator = "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)(\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)){3}"

data class UdpRecordAddressDto(
    val id: Long,
    @Pattern(regexp= ipRegexpValidator, message = "Incorrect IP address")
    val listeningIp: String,
    @field:Range(min = 1, max = 65535)
    val listeningPort: Int,
    @Pattern(regexp= ipRegexpValidator, message = "Incorrect IP address")
    val destinationIp: String,
    @field:Range(min = 1, max = 65535)
    val destinationPort: Int
)