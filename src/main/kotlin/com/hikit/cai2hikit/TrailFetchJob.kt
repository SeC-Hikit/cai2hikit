package com.hikit.cai2hikit

import com.hikit.cai2hikit.dao.Trail
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class TrailFetchJob(restClientBuilder: RestClient.Builder) {

    private val restClient: RestClient

    init {
        restClient = restClientBuilder.baseUrl("http://osm2cai.it/api/v2").build()
    }

    @Scheduled(fixedRate = 5000)
    fun getTrail() {
        println(restClient.get().uri("/hiking-route/11581").retrieve().body(Trail::class.java))
    }
}