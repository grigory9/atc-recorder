package com.example.recorder.protocol.mapper

import com.example.recorder.model.UdpRecordFolder
import com.example.recorder.protocol.UdpRecordFolderDto
import org.springframework.stereotype.Component

@Component
class UdpRecordFolderMapper {

    fun map(dto: UdpRecordFolderDto) = UdpRecordFolder(
        folder = dto.folderPath
    )

    fun map(model: UdpRecordFolder) = UdpRecordFolderDto(
        folderPath = model.folder
    )

}
