package com.hikit.cai2hikit.processor

import com.hikit.cai2hikit.TrailRepository
import com.hikit.cai2hikit.adapter.AltitudeServiceWrapper
import com.hikit.cai2hikit.dto.Coordinates2D
import com.hikit.cai2hikit.dto.Trail
import org.hikit.common.processor.Coordinates
import org.hikit.common.processor.TrailsStatsCalculator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component


@Component
class TrailSimilarityAlgorithm @Autowired constructor(
    val trailRepository: TrailRepository,
    val dtwAlgorithm: DTWAlgorithm,
    val altitudeServiceAdapter: AltitudeServiceWrapper
) {
    fun runAlgorithm(trailIn_0_Id: String, trailIn_1: Trail): Double {
        val trailStatsCalculator = TrailsStatsCalculator()
        val trailIn_0 = trailRepository.findByPropsId(trailIn_0_Id)
        var scores = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0)

        if (trailIn_0 != null) {
            val trailWithoutElevation: List<Coordinates2D> = trailIn_0.geometry.coordinates[0]
            val trailWithElevation: List<Coordinates> = altitudeServiceAdapter.mapCoordsWithElevations(trailWithoutElevation)

            println(trailWithElevation[0])
        }

        val trailScore = dtwAlgorithm.runAlgorithm(trailIn_0_Id, trailIn_1)

        return 1.0
    }
}