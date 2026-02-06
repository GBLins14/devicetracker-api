package com.gblins.devicetracker.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "pings")
data class DevicePing(
    @Id
    val id: String? = null,
    val deviceId: String,
    val firmwareVersion: String,
    val timestamp: LocalDateTime,
    val latitude: Double,
    val longitude: Double
)