package com.hikit.cai2hikit

import com.hikit.cai2hikit.dao.Geometry as daoGeometry
import com.hikit.cai2hikit.dao.GeometryMapper
import org.hikit.common.dto.IdToUpdateDate
import org.hikit.common.dto.Trail as dtoTrail
import com.hikit.cai2hikit.dao.Trail as daoTrail
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class TrailFetchJob(
    val trailRestClient: TrailRestClient,
    val trailRepository: TrailRepository,
    val geometryMapper: GeometryMapper
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
        fetchedTrail: dtoTrail,
        trailToLastUpdate: IdToUpdateDate
    ) {
        val previouslySavedTrail = trailRepository.findByPropsId(fetchedTrail.properties.id)
//        println(fetchedTrail.geometry)
        if (previouslySavedTrail == null) {
            trailRepository.insert(fetchedTrail)
        } else if (previouslySavedTrail.properties.updatedAt < fetchedTrail.properties.updatedAt) {
            logger.info("Trail with id ${trailToLastUpdate.id} updated by newly fetched $fetchedTrail")
            previouslySavedTrail.properties = fetchedTrail.properties
            previouslySavedTrail.geometry = fetchedTrail.geometry
            trailRepository.save(previouslySavedTrail)
        } else {
            logger.debug("Trail with id ${trailToLastUpdate.id} is already up to date")
        }
    }
}