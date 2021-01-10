package com.example.recorder.controller

import com.example.recorder.protocol.ErrorDto
import com.example.recorder.protocol.RecordDeviceDto
import com.example.recorder.protocol.mapper.RecordDeviceMapper
import com.example.recorder.service.ConfigurationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("configuration/device")
class DeviceConfigurationController(
    private val service: ConfigurationService,
    private val mapper: RecordDeviceMapper
) {
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    //region Swagger
    @Operation(summary = "Setup device", description = "Setup device for record", tags = ["udp"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Device ready",
                content = []
            )
        ]
    )
    //endregion
    fun setupDevice(@RequestBody dto: RecordDeviceDto) = service.setupDevice(mapper.map(dto))

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    //region Swagger
    @Operation(summary = "Get device list", description = "Find all current and possible devices", tags = ["udp"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "OK",
                content = [Content(schema = Schema(implementation = RecordDeviceDto::class))]
            ),
            ApiResponse(
                responseCode = "400", description = "Bad request",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "404", description = "Not found",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            ),
            ApiResponse(
                responseCode = "500", description = "Internal error",
                content = [Content(schema = Schema(implementation = ErrorDto::class))]
            )
        ]
    )
    //endregion
    fun findAll() = service.findAllDevices()
}
