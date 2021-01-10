package com.example.recorder.controller

import com.example.recorder.service.CommandService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus.OK
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("record")
class RecordController(
    private val service: CommandService
) {
    @PostMapping("start")
    @ResponseStatus(OK)
    //region Swagger
    @Operation(summary = "Start record", description = "Start record", tags = ["start", "record"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Start record success",
                content = []
            )
        ]
    )
    //endregion
    fun start() = service.startRecord()

    @PostMapping("stop")
    @ResponseStatus(OK)
    //region Swagger
    @Operation(summary = "Stop record", description = "Stop record", tags = ["stop", "record"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Stop record success",
                content = []
            )
        ]
    )
    //endregion
    fun stop() = service.stopRecord()
}