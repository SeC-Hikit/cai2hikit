package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.IdToUpdateDate
import com.hikit.cai2hikit.dto.Trail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


private const val dateTimeFormat = "yyyy-MM-dd HH:mm:ss"

@Service
class TrailFetchJob(
        @Value("\${job.fetch.bblatmin}") val bblatmin: String,
        @Value("\${job.fetch.bblatmax}") val bblatmax: String,
        @Value("\${job.fetch.bblongmin}") val bblongmin: String,
        @Value("\${job.fetch.bblongmax}") val bblongmax: String
    ) {

    @Autowired
    private lateinit var trailRepository: TrailRepository

    @Autowired
    private lateinit var lastUpdateRepository: LastUpdateRepository

    private val restClient: RestClient = RestClient.builder().baseUrl("https://osm2cai.it/api/v2/").build()

    @Scheduled(cron = "\${job.fetch.chron}")
    fun getTrail() {
//        println(trailRepository.findAll())
        val uriString = "hiking-routes/bb/$bblatmax,$bblatmin,$bblongmin,$bblongmax/4"
        println(uriString)

        // TODO make the bounding box configurable
        val trailsResponse = restClient.get()
            .uri(uriString)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Map::class.java)
            ?.map {
                IdToUpdateDate(
                    it.key.toString(),
                    LocalDateTime.parse(it.value.toString(), DateTimeFormatter.ofPattern(dateTimeFormat))
                )
            }
        //TODO : store in DB
        if (trailsResponse != null) {
            Collections.sort<IdToUpdateDate>(
                trailsResponse,
                Comparator<IdToUpdateDate> { lhs, rhs ->
                    if (lhs.lastUpdateDate > rhs.lastUpdateDate) -1
                    else if ((lhs.lastUpdateDate < rhs.lastUpdateDate)) 1
                    else 0
                })
//            println(trailsResponse)
            val lastUpdate = lastUpdateRepository.findAll()[0].lastUpdateDate
            for(trailId in trailsResponse) {
                if (trailId.lastUpdateDate > lastUpdate) {
                    println(trailId.lastUpdateDate)
                    println(lastUpdate)
                    val serializedResponse = restClient.get()
                        .uri("hiking-route/${trailId.id}")
                        .accept(MediaType.APPLICATION_JSON)
                        .retrieve()
                        .body(Trail::class.java)

                    println(serializedResponse)
                    if (serializedResponse != null) {
                        trailRepository.save(Trail(serializedResponse.properties, serializedResponse.geometry))
                    }
                    Thread.sleep(1_000)
                }
            }
            lastUpdateRepository.deleteAll()
            lastUpdateRepository.save(trailsResponse[0])
        }
    }
}