package com.gblins.devicetracker

import io.github.cdimascio.dotenv.dotenv
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class DeviceTrackerApplication

fun main(args: Array<String>) {
    val dotenv = dotenv()
    dotenv.entries().forEach { entry ->
        System.setProperty(entry.key, entry.value)
    }

    runApplication<DeviceTrackerApplication>(*args)
}
