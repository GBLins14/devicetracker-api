package com.gblins.devicetracker.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "security")
data class SecurityConfig(
    var apiKey: String = ""
)