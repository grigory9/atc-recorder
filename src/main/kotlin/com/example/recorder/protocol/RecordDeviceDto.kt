package com.example.recorder.protocol

data class RecordDeviceDto(
    val id: Long,
    val deviceName: String,
    val isPrimary: Boolean
)