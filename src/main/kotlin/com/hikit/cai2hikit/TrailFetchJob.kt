package com.hikit.cai2hikit

import com.hikit.cai2hikit.remote.OsmTrail
import com.hikit.cai2hikit.remote.FetchedTrailMapper
import org.hikit.common.dto.IdToUpdateDate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class TrailFetchJob(
    val trailRestClient: TrailRestClient,
    val trailRepository: TrailRepository,
    val fetchedTrailMapper: FetchedTrailMapper
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
            upsertMoreRecentData(fetchedTrail, trailToLastUpdate)
            Thread.sleep(200)
        }
    }

    private fun upsertMoreRecentData(
        fetchedTrail: OsmTrail,
        trailToLastUpdate: IdToUpdateDate
    ) {
        val previouslySavedTrail = trailRepository.findByPropsId(fetchedTrail.properties.id)
        val trailForSaving = fetchedTrailMapper.mapToEntity(fetchedTrail)
        if (previouslySavedTrail == null) {
            trailRepository.insert(trailForSaving)
        } else if (previouslySavedTrail.properties.updatedAt < fetchedTrail.properties.updatedAt) {
            logger.info("Trail with id ${trailToLastUpdate.id} updated by newly fetched $fetchedTrail")
            previouslySavedTrail.properties = trailForSaving.properties
            previouslySavedTrail.geometry = trailForSaving.geometry
            trailRepository.save(previouslySavedTrail)
        } else {
            logger.debug("Trail with id ${trailToLastUpdate.id} is already up to date")
        }
    }
}