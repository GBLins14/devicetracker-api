package com.gblins.devicetracker.repository

import com.gblins.devicetracker.model.DevicePing
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository

@Repository
interface DevicePingRepository : ReactiveMongoRepository<DevicePing, String> {

}