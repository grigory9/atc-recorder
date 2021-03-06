package com.example.recorder.controller

import com.example.recorder.protocol.PlaybackDumpOptionsDto
import com.example.recorder.protocol.mapper.PlaybackStartOptionsMapper
import com.example.recorder.protocol.PlaybackStartOptionsDto
import com.example.recorder.protocol.mapper.PlaybackDumpOptionsMapper
import com.example.recorder.service.CommandService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("playback")
class PlaybackController(
    private val service: CommandService,
    private val mapper: PlaybackStartOptionsMapper,
    private val dumpMapper: PlaybackDumpOptionsMapper
) {
    @PostMapping("start")
    @ResponseStatus(OK)
    //region Swagger
    @Operation(summary = "Start playback", description = "Start playback", tags = ["start", "playback"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Start playback success",
                content = []
            )
        ]
    )
    //endregion
    fun start(@RequestBody dto: PlaybackStartOptionsDto) = service.startPlayback(mapper.map(dto))

    @PostMapping("dump", produces = [MediaType.APPLICATION_OCTET_STREAM_VALUE])
    @ResponseStatus(OK)
    //region Swagger
    @Operation(summary = "Start playback", description = "Start playback", tags = ["start", "playback"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Start playback success",
                content = []
            )
        ]
    )
    //endregion
    fun dump(@RequestBody dto: PlaybackDumpOptionsDto) = service.dump(dumpMapper.map(dto))

    @PostMapping("stop")
    @ResponseStatus(OK)
    //region Swagger
    @Operation(summary = "Stop playback", description = "Stop playback", tags = ["stop", "playback"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Stop playback success",
                content = []
            )
        ]
    )
    //endregion
    fun stop() = service.stopPlayback()
}