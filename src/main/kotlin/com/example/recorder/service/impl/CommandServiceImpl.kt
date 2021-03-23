package com.example.recorder.service.impl

import com.example.recorder.domain.*
import com.example.recorder.model.PlaybackStartOptions
import com.example.recorder.model.PlaybackDumpOptions
import com.example.recorder.repository.UdpRecordAddressRepository
import com.example.recorder.service.CommandService
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.springframework.stereotype.Service
import java.util.*

@Service
class CommandServiceImpl(
    val recorderUseCase: RecorderUseCase,
    val playbackUseCase: PlaybackUseCase,
    val dataHandlerUseCase: DataHandlerUseCase,
    val packetsSendUseCase: PacketsSendUseCase,
    val pcapDumpUseCase: PcapDumpUseCase
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

    override fun dump(options: PlaybackDumpOptions): ByteArray {
        pcapDumpUseCase.readPacketDumpCallback = { to: Date, from: Date -> dataHandlerUseCase.readDump(to, from) }
        return pcapDumpUseCase.getDump(options.fromDate, options.toDate)
    }

    override fun stopPlayback() {
        GlobalScope.async {
            playbackUseCase.stop()
        }
    }
}