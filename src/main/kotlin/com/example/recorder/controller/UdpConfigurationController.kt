package com.example.recorder.controller

import com.example.recorder.protocol.ErrorDto
import com.example.recorder.protocol.UdpRecordAddressDto
import com.example.recorder.protocol.UdpRecordFolderDto
import com.example.recorder.protocol.mapper.UdpRecordAddressMapper
import com.example.recorder.protocol.mapper.UdpRecordFolderMapper
import com.example.recorder.service.ConfigurationService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("configuration/udp")
class UdpConfigurationController(
    private val service: ConfigurationService,
    private val addressMapper: UdpRecordAddressMapper,
    private val folderMapper: UdpRecordFolderMapper
) {
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    //region Swagger
    @Operation(summary = "Add UDP address", description = "Add UDP address for record", tags = ["udp"])
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "UDP address for record added",
                content = []
            )
        ]
    )
    //endregion
    fun addUdp(@RequestBody dto: UdpRecordAddressDto) = service.addUdpRecordAddress(addressMapper.map(dto))

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    //region Swagger
    @Operation(
        summary = "Returns a list of all udp record addresses",
        description = "Returns a list of all udp record addresses",
        tags = ["udp-record-address-list"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "OK",
                content = [Content(schema = Schema(implementation = UdpRecordAddressDto::class))]
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
    fun findAll() = service.findAllUdpRecordAddress()

    @PostMapping("recordFolder")
    @ResponseStatus(HttpStatus.CREATED)
    //region Swagger
    @Operation(
        summary = "Change record folder",
        description = "Change record folder for udp recording",
        tags = ["udp", "folder"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "Folder for udp recording changed",
                content = []
            )
        ]
    )
    //endregion
    fun changeUdpRecordFolder(@RequestBody dto: UdpRecordFolderDto) =
        service.changeUdpRecordFolder(folderMapper.map(dto))

    @GetMapping("recordFolder")
    @ResponseStatus(HttpStatus.OK)
    //region Swagger
    @Operation(
        summary = "Returns a udp record folder path",
        description = "Returns a udp record folder path",
        tags = ["udp", "folder"]
    )
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200", description = "OK",
                content = [Content(schema = Schema(implementation = UdpRecordFolderDto::class))]
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
    fun findUdpRecordFolder() = service.findUdpRecordFolder()
}
