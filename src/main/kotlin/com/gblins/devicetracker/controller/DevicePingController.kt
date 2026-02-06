package com.gblins.devicetracker.controller

import com.gblins.devicetracker.model.DevicePing
import com.gblins.devicetracker.service.DevicePingService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/api/v1/pings")
class DevicePingController(
    private val service: DevicePingService
) {
    @PostMapping
    fun registerPing(@RequestBody ping: DevicePing): Mono<DevicePing> = service.savePing(ping)

    @GetMapping
    fun getAllPings(): Flux<DevicePing> = service.getAllPings()
}