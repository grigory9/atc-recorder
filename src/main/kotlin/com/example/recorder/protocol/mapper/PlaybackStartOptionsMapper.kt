package com.example.recorder.protocol.mapper

import com.example.recorder.model.PlaybackStartOptions
import com.example.recorder.protocol.PlaybackStartOptionsDto
import org.springframework.stereotype.Component

@Component
class PlaybackStartOptionsMapper {

    fun map(dto: PlaybackStartOptionsDto) = PlaybackStartOptions(
        startDate = dto.startDate
    )

    fun map(model: PlaybackStartOptions) = PlaybackStartOptionsDto(
        startDate = model.startDate
    )

}