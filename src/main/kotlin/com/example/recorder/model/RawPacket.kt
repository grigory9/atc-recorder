package com.example.recorder.model

import java.util.*
data class RawPacket(
    val byteArray: ByteArray,
    val timestamp: Date
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RawPacket

        if (!byteArray.contentEquals(other.byteArray)) return false
        if (timestamp != other.timestamp) return false

        return true
    }

    override fun hashCode(): Int {
        var result = byteArray.contentHashCode()
        result = 31 * result + timestamp.hashCode()
        return result
    }
}