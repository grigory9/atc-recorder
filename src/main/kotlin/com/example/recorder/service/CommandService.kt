package com.example.recorder.service

import com.example.recorder.model.PlaybackDumpOptions
import com.example.recorder.model.PlaybackStartOptions

interface CommandService {

    fun startRecord()
    fun stopRecord()

    fun startPlayback(options: PlaybackStartOptions)
    fun stopPlayback()

    fun dump(options: PlaybackDumpOptions)
}