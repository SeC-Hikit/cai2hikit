package com.hikit.cai2hikit

import com.hikit.cai2hikit.remote.OsmTrail
import com.hikit.cai2hikit.remote.FetchedTrailMapper
import org.hikit.common.dto.IdToUpdateDate
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class TrailUpdateHelper(
    val trailRepository: TrailRepository,
    val fetchedTrailMapper: FetchedTrailMapper
) {
    private val logger: Logger = LoggerFactory.getLogger(TrailUpdateHelper::class.java)


    @Transactional(rollbackFor = [Exception::class])
    fun upsertMoreRecentData(
        fetchedTrail: OsmTrail,
        trailToLastUpdate: IdToUpdateDate
    ) {
        val previouslySavedTrail = trailRepository.findByPropsId(fetchedTrail.properties.id)
        val trailForSaving = fetchedTrailMapper.mapToEntity(fetchedTrail)
        if (previouslySavedTrail == null) {
            logger.info("Saving newly detected trail ${trailForSaving.properties.id}")
            trailRepository.insert(trailForSaving)
        } else if (previouslySavedTrail.properties.updatedAt < fetchedTrail.properties.updatedAt) {
            logger.info("Trail with id ${trailToLastUpdate.id} updated by newly fetched $fetchedTrail")
            trailRepository.delete(previouslySavedTrail)
            trailRepository.save(trailForSaving)
        } else {
            logger.debug("Trail with id ${trailToLastUpdate.id} is already up to date")
        }
    }
}