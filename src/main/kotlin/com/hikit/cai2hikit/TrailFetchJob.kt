package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Trail
import org.springframework.http.MediaType
import org.springframework.scheduling.Trigger
import org.springframework.scheduling.TriggerContext
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.time.Instant

@Service
class TrailFetchJob {

    private val restClient: RestClient = RestClient.builder().baseUrl("https://osm2cai.it/api/v2/").build()

    fun getTrail() {
        // TODO: get all ER trails

        // TODO: persist all of them querying them one by one with a short interruption in the middle (~1s)
        val serializedResponse = restClient.get()
            .uri("hiking-route/11581")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Trail::class.java)

        // TODO exclude non relevant trails
        println(serializedResponse)
        // TODO
    }

}