package com.example.recorder.service.impl

import com.example.recorder.domain.PacketsSendUseCase
import com.example.recorder.domain.DataHandlerUseCase
import com.example.recorder.model.PlaybackStartOptions
import com.example.recorder.domain.PlaybackUseCase
import com.example.recorder.domain.RecorderUseCase
import com.example.recorder.repository.UdpRecordAddressRepository
import com.example.recorder.service.CommandService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.stereotype.Service

@Service
class CommandServiceImpl(
    val addressRepository: UdpRecordAddressRepository,
    val recorderUseCase: RecorderUseCase,
    val playbackUseCase: PlaybackUseCase,
    val dataHandlerUseCase: DataHandlerUseCase,
    val packetsSendUseCase: PacketsSendUseCase
) : CommandService {

    override fun startRecord() {
        GlobalScope.async {
            recorderUseCase.packetCatchHandler = { dataHandlerUseCase.writeUdp(it) }
            recorderUseCase.start()
        }
    }

    override fun stopRecord() {
        GlobalScope.async {
            recorderUseCase.stop()
        }
    }

    override fun startPlayback(options: PlaybackStartOptions) {
        GlobalScope.async {
            playbackUseCase.readPacketBatchCallback = { dataHandlerUseCase.readBatch(it) }
            playbackUseCase.sendPacketsCallback = { packetsSendUseCase.sendPackets(it) }
            playbackUseCase.start(options)
        }
    }

    override fun stopPlayback() {
        GlobalScope.async {
            playbackUseCase.stop()
        }
    }
}