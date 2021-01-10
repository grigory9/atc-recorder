package com.example.recorder

import org.springframework.boot.Banner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BeautyClubApplication

fun main(args: Array<String>) {
    runApplication<BeautyClubApplication>(*args) {
        setBannerMode(Banner.Mode.OFF)
    }
}
