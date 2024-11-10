package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Trail
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

@Service
class TrailFetchJob() {

//    private val restClient: RestClient = RestClient.builder().baseUrl("http://osm2cai.it/api/v2/").build()
    private val restClient: RestClient = RestClient.create()

    @Scheduled(fixedRate = 10000)
    fun getTrail() {
        println(restClient.get()
            .uri("http://osm2cai.it/api/v2/hiking-route/11581")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Trail::class.java))
    }

}