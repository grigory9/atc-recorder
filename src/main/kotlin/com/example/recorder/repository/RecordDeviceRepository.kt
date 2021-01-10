package com.example.recorder.repository

import com.example.recorder.model.RecordDevice
import org.springframework.data.repository.Repository

interface RecordDeviceRepository: Repository<RecordDevice, Long> {

    fun save(recordDevice: RecordDevice): RecordDevice

    fun findAll(): List<RecordDevice>

    fun findById(recordDeviceId: Long): List<RecordDevice>

    fun deleteById(recordDeviceId: Long)

}