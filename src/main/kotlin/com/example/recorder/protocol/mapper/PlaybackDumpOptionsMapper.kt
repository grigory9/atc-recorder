package com.example.recorder.protocol.mapper

import com.example.recorder.model.PlaybackDumpOptions
import com.example.recorder.protocol.PlaybackDumpOptionsDto
import org.springframework.stereotype.Component

@Component
class PlaybackDumpOptionsMapper {

    fun map(dto: PlaybackDumpOptionsDto) = PlaybackDumpOptions(
        toDate = dto.toDate,
        fromDate = dto.fromDate
    )

    fun map(model: PlaybackDumpOptions) = PlaybackDumpOptionsDto(
        toDate = model.toDate,
        fromDate = model.fromDate
    )

}