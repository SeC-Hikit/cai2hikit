package com.hikit.cai2hikit.testconfig

import com.hikit.cai2hikit.TrailRestClient
import org.mockito.Mockito
import org.springframework.context.annotation.Bean
/*
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
class RestClientConfiguration {
}
@Profile("test")
@Primary
*/
@Bean
fun getTrailRestClient(): TrailRestClient {
    return Mockito.mock(TrailRestClient::class.java)
}
