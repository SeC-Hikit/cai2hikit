package com.hikit.cai2hikit

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class TrailFetchJob(
    val trailRestClient: TrailRestClient,
    val trailRepository: TrailRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(TrailFetchJob::class.java)

    @Scheduled(cron = "\${job.fetch.chron}")
    fun updateSystem() {
        val fetchTrailIdsWithinBoundBox = trailRestClient.fetchTrailIdsWithinBoundBox()
        for (trailToLastUpdate in fetchTrailIdsWithinBoundBox) {
            val fetchedTrail = trailRestClient.fetchTrail(trailToLastUpdate.id)
            if (fetchedTrail == null) {
                logger.error("Could not fetch trail with id ${trailToLastUpdate.id}")
            }
            val previouslySavedTrail = trailRepository.findByPropsId(fetchedTrail!!.properties.id)
            if(previouslySavedTrail == null){
                trailRepository.insert(fetchedTrail)
            } else if(previouslySavedTrail.properties.updatedAt < fetchedTrail.properties.updatedAt) {
                logger.info("Trail with id ${trailToLastUpdate.id} updated by newly fetched $fetchedTrail")
                // TODO: override data
            } else {
                logger.debug("Trail with id ${trailToLastUpdate.id} is already up to date")
            }
            Thread.sleep(1_000)
        }
    }
}