package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Trail
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class TrailFetchJob {

    private val restClient: RestClient = RestClient.builder().baseUrl("https://osm2cai.it/api/v2/").build()

    @Scheduled(cron = "\${cron.cr-expr}")
    fun getTrail() {
        val serializedResponse = restClient.get()
            .uri("hiking-route/11581")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Trail::class.java)

        println(serializedResponse)
        // TODO
    }

}