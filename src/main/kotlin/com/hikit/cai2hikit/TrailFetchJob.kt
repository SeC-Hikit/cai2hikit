package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.IdToUpdateDate
import com.hikit.cai2hikit.dto.Trail
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val dateTimeFormat = "yyyy-MM-dd HH:mm:ss"

@Service
class TrailFetchJob {

    private val restClient: RestClient = RestClient.builder().baseUrl("https://osm2cai.it/api/v2/").build()

    @Scheduled(cron = "\${job.fetch.chron}")
    fun getTrail() {


        // TODO make the bounding box configurable
        val trailsResponse = restClient.get()
            .uri("hiking-routes/bb/44.810096,43.741833,11.355102,12.036322/4")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Map::class.java)
            ?.map {
                // TODO: test this
                IdToUpdateDate(
                    it.key.toString(),
                    LocalDateTime.parse(it.value.toString(), DateTimeFormatter.ofPattern(dateTimeFormat))
                )
            }

        // automatically casts to java.util.LinkedHashMap
        println(trailsResponse!!::class.java.typeName)
        println(trailsResponse)

        // TODO: loop over all trails and get the data by hike id

        val hike_id = 11581
        // TODO: persist all of them querying them one by one with a short interruption in the middle (~1s)
        val serializedResponse = restClient.get()
            .uri("hiking-route/${hike_id}")
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Trail::class.java)
    }
}