package com.gblins.devicetracker.service

import com.gblins.devicetracker.model.DevicePing
import com.gblins.devicetracker.repository.DevicePingRepository
import org.springframework.stereotype.Service
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Service
class DevicePingService(
    private val repository: DevicePingRepository
) {
    fun savePing(ping: DevicePing): Mono<DevicePing> = repository.save(ping)

    fun getAllPings(): Flux<DevicePing> = repository.findAll()
}