package com.example.recorder.configuration

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class BeautyClubConfiguration {

    @Bean
    @Primary
    fun objectMapper(): ObjectMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .registerModule(JavaTimeModule())
        .disable(DeserializationFeature.ACCEPT_FLOAT_AS_INT)
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

}