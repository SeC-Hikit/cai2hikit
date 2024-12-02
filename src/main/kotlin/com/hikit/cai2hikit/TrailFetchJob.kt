package com.hikit.cai2hikit

import org.springframework.data.mongodb.repository.MongoRepository

import com.hikit.cai2hikit.dto.IdToUpdateDate
import com.hikit.cai2hikit.dto.Trail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.slf4j.Logger
import org.slf4j.LoggerFactory

private const val dateTimeFormat = "yyyy-MM-dd HH:mm:ss"

@Service
class TrailFetchJob(
    private val trailRestClient: TrailRestClient = getTrailRestClient("https://osm2cai.it/api/v2/"),
    @Value("\${job.fetch.bblatmin}") val bblatmin: String,
    @Value("\${job.fetch.bblatmax}") val bblatmax: String,
    @Value("\${job.fetch.bblongmin}") val bblongmin: String,
    @Value("\${job.fetch.bblongmax}") val bblongmax: String
    ) {

    @Autowired
    private lateinit var trailRepository: TrailRepository

    var logger: Logger = LoggerFactory.getLogger(TrailFetchJob::class.java)

    @Scheduled(cron = "\${job.fetch.chron}")
    fun getTrail() {
        val boloHikesListUri = "hiking-routes/bb/$bblatmax,$bblatmin,$bblongmin,$bblongmax/4"

        val trailsResponse = trailRestClient.getRestClient().get()
            .uri(boloHikesListUri)
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
            for(trailId in trailsResponse) {
                val serializedResponse = trailRestClient.getRestClient().get()
                    .uri("hiking-route/${trailId.id}")
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .body(Trail::class.java)

                logger.info(serializedResponse.toString())

                if (serializedResponse != null) {
                    trailRepository.save(Trail(serializedResponse.properties, serializedResponse.geometry))
                }
                Thread.sleep(1_000)
            }
        }
    }
}