package com.hikit.cai2hikit

import org.hikit.common.dto.IdToUpdateDate
import com.hikit.cai2hikit.dao.Trail
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClientException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val sda = 4
private const val dateTimeFormat = "yyyy-MM-dd HH:mm:ss"
private var logger: Logger = LoggerFactory.getLogger(TrailFetchJob::class.java)

@Component
class TrailRestClient(
    private val restClient: RestClient,
    @Value("\${job.fetch.bblatmin}") val bblatmin: String,
    @Value("\${job.fetch.bblatmax}") val bblatmax: String,
    @Value("\${job.fetch.bblongmin}") val bblongmin: String,
    @Value("\${job.fetch.bblongmax}") val bblongmax: String
) {
    fun fetchTrailIdsWithinBoundBox(): List<IdToUpdateDate> {
        logger.info("Going to fetch trails IDs from remote")
        val trailListUri = "hiking-routes/bb/$bblongmin,$bblatmin,$bblongmax,$bblatmax/$sda"
        val idToLastUpdatedDate = restClient.getClient().get()
            .uri(trailListUri)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Map::class.java)
            ?.map {
                IdToUpdateDate(
                    it.key.toString(),
                    LocalDateTime.parse(it.value.toString(), DateTimeFormatter.ofPattern(dateTimeFormat))
                )
            }
        if(idToLastUpdatedDate == null) {
            return emptyList()
        }
        logger.info("Done fetching trails IDs from remote")
        return idToLastUpdatedDate
    }

    fun fetchTrail(id: String): Trail? {
        logger.info("Going to fetch full trail with Id '$id' from remote")
        val trailUri = "hiking-route/${id}"
        try {
            val serializedResponse = restClient.getClient().get()
                .uri(trailUri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .body(Trail::class.java)
            if (serializedResponse != null) {
                println(serializedResponse.properties)
            }
            logger.info("Done fetching full trail with Id '$id' from remote")
            return serializedResponse
        } catch (ex: RestClientException) {
            logger.info("Error with fetching trail with $id' from remote, cause: ${ex.message}" )
            return null
        }
    }

}