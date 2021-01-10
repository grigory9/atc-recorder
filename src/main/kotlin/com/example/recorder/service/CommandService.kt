package com.example.recorder.service

import com.example.recorder.model.PlaybackStartOptions

interface CommandService {

    fun startRecord()
    fun stopRecord()

    fun startPlayback(options: PlaybackStartOptions)
    fun stopPlayback()

}