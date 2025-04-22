package com.hikit.cai2hikit

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class TrailFetchJob(
    val trailRestClient: TrailRestClient,
    val trailUpdateHelper: TrailUpdateHelper
) {
    private val logger: Logger = LoggerFactory.getLogger(TrailFetchJob::class.java)

    @Scheduled(cron = "\${job.fetch.chron}")
    fun updateSystem() {
        val fetchTrailIdsWithinBoundBox = trailRestClient.fetchTrailIdsWithinBoundBox()
        for (trailToLastUpdate in fetchTrailIdsWithinBoundBox) {
            val fetchedTrail = trailRestClient.fetchTrail(trailToLastUpdate.id)
            if (fetchedTrail == null) {
                logger.error("Could not fetch trail with id ${trailToLastUpdate.id}")
                continue
            }
            if (fetchedTrail.properties.ref == null) {
                logger.warn("Could fetch trail with id ${trailToLastUpdate.id}, but Ref Id has been found 'null'. Skip saving")
                continue
            }
            trailUpdateHelper.upsertMoreRecentData(fetchedTrail, trailToLastUpdate)
            Thread.sleep(200)
        }
    }
}