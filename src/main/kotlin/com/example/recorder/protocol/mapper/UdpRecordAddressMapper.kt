package com.example.recorder.protocol.mapper

import com.example.recorder.model.RecordDevice
import com.example.recorder.protocol.RecordDeviceDto
import org.springframework.stereotype.Component

@Component
class RecordDeviceMapper {

    fun map(dto: RecordDeviceDto) = RecordDevice(
        deviceName = dto.deviceName,
        isPrimary = dto.isPrimary
    )

    fun map(model: RecordDevice) = RecordDeviceDto(
        id = model.id!!,
        deviceName = model.deviceName,
        isPrimary = model.isPrimary
    )

}
