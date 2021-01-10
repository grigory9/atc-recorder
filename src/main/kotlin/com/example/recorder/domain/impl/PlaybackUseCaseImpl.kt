package com.example.recorder.domain.impl

import com.example.recorder.domain.PlaybackUseCase
import com.example.recorder.model.PlaybackStartOptions
import com.example.recorder.model.UdpPacket
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.channels.sendBlocking
import org.pcap4j.packet.Packet
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

@Component
class PlaybackUseCaseImpl(): PlaybackUseCase {
    private var simTime: Date? = null
    private var currentSimTime: Date? = null
    private var startTime: Date? = null
    private var udpPacketBatch = listOf<UdpPacket>()
    private var dispatcher = newSingleThreadContext("Playback thread")
    private var courotineFinisher: SendChannel<FinishMsg>? = null
    private var currentBatchHashCode: Int? = null

    override var readPacketBatchCallback: ((Date) -> List<UdpPacket>)? = null
    override var sendPacketsCallback: ((List<UdpPacket>) -> Unit)? = null

    override suspend fun start(options: PlaybackStartOptions) {
        stop()

        if (courotineFinisher != null) {
            var response = CompletableDeferred<Boolean>()
            courotineFinisher?.send(IsFinished(response))
            response.await()
        }

        simTime = options.startDate
        currentSimTime = options.startDate
        startTime = Date()

        updatePacketBatch(options.startDate)
        launch()
    }

    override suspend fun stop() {
        if(courotineFinisher != null ) {
            courotineFinisher?.send(Finish)
        } else {
            clear()
        }
    }

    private fun clear() {
        simTime = null
        startTime = null
        udpPacketBatch = listOf()
        currentBatchHashCode = null
    }

    private suspend fun launch() = coroutineScope {
        launch(dispatcher) {
            courotineFinisher = finisherActor()
            while (true) {
                updateSimTime()
                val simTime = simTime
                if (simTime != null) {
                    if (udpPacketBatch.count() == 0) {
                        updatePacketBatch(simTime)
                    }
                    val packetsForSend = takeOutUdpPacketsFromBatch(simTime)
                    sendPacketsCallback?.let { it(packetsForSend) }
                }

                var response = CompletableDeferred<Boolean>()
                courotineFinisher?.send(IsFinished(response))
                if(response.await()) {
                    clear()
                    break
                }
            }
        }
    }

    private fun updateSimTime() {
        val time = currentSimTime?.time
        val startTime = startTime
        if (time != null && startTime != null) {
            simTime = Date(time + (Date().time - startTime.time))
        }
    }

    private fun updatePacketBatch(date: Date) {
        val batch = readPacketBatchCallback?.let { it(date) }

        if (batch != null && batch.hashCode() != currentBatchHashCode) {
            udpPacketBatch = batch
            currentBatchHashCode = batch.hashCode()
        }
    }

    private fun takeOutUdpPacketsFromBatch(date: Date): List<UdpPacket> {
        val droppedPackets = udpPacketBatch.takeWhile { it.date.time <= date.time }
        if (droppedPackets.count() > 0) {
            println("DroppedPacketsCount=${droppedPackets.count()}")
        }
        udpPacketBatch = udpPacketBatch.subList(droppedPackets.count(), udpPacketBatch.count())
        return droppedPackets
    }
}

// Message types for counterActor
sealed class FinishMsg
object Finish : FinishMsg()
class IsFinished(val response: CompletableDeferred<Boolean>) : FinishMsg()

fun CoroutineScope.finisherActor() = actor<FinishMsg> {
    var isFinished = false
    for (msg in channel) { // iterate over incoming messages
        when (msg) {
            is Finish -> isFinished = true
            is IsFinished-> msg.response.complete(isFinished)
        }
    }
}