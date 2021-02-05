package com.mariuszf.flatconfig.adapters.messaging

import org.springframework.cloud.stream.function.StreamBridge
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KafkaConfiguration {

    @Bean
    fun flatConfigInfoSync(streamBridge: StreamBridge) = FlatConfigInfoSyncImpl(streamBridge)

}