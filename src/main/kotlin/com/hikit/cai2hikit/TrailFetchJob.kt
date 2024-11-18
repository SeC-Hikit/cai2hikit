package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Trail
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class TrailFetchJob {

    private val restClient: RestClient = RestClient.builder().baseUrl("https://osm2cai.it/api/v2/").build()

    @Scheduled(cron = "\${job.fetch.chron}")
    fun getTrail() {
        // TODO: get all ER trails
        // BB should be bigger
        val trailsResponse = restClient.get()
            .uri("hiking-routes/bb/44.810096,43.741833,11.355102,12.036322/4")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(object : ParameterizedTypeReference<Any?>() {})

        println(trailsResponse)

        // TODO: persist all of them querying them one by one with a short interruption in the middle (~1s)
        val serializedResponse = restClient.get()
            .uri("hiking-route/11581")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Trail::class.java)

        // TODO exclude non relevant trails
        println(serializedResponse)
    }

}