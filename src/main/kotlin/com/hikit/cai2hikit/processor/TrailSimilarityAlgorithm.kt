package com.hikit.cai2hikit.processor

import com.hikit.cai2hikit.TrailRepository
import com.hikit.cai2hikit.adapter.AltitudeServiceWrapper
import com.hikit.cai2hikit.dto.Coordinates2D
import com.hikit.cai2hikit.dto.Trail
import org.hikit.common.processor.Coordinates
import org.hikit.common.processor.TrailsStatsCalculator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.math.max
import kotlin.math.pow


@Component
class TrailSimilarityAlgorithm @Autowired constructor(
    val trailRepository: TrailRepository,
    val dtwAlgorithm: DTWAlgorithm,
    val altitudeServiceAdapter: AltitudeServiceWrapper
) {
    fun runAlgorithm(trailIn_0_Id: String, trailIn_1: Trail, trailIn_1_metadata: Array<Double>): Double {
        val trailStatsCalculator = TrailsStatsCalculator()
        val trailIn_0 = trailRepository.findByPropsId(trailIn_0_Id)

        var metaScores: Array<Double> = arrayOf()

        if (trailIn_0 != null) {
            val trailWithoutElevation: List<Coordinates2D> = trailIn_0.geometry.coordinates[0]
            val trailWithElevation: List<Coordinates> = altitudeServiceAdapter.mapCoordsWithElevations(trailWithoutElevation)

            metaScores = arrayOf(
                1.0 / max(1.0, ((trailIn_1_metadata[0] - trailStatsCalculator.calculateTotRise(trailWithElevation))).pow(2)),
                1.0 / max(1.0, ((trailIn_1_metadata[1] - trailStatsCalculator.calculateTotFall(trailWithElevation))).pow(2)),
                1.0 / max(1.0, ((trailIn_1_metadata[2] - trailStatsCalculator.calculateTrailLength(trailWithElevation))).pow(2)),
                1.0 / max(1.0, ((trailIn_1_metadata[3] - trailStatsCalculator.calculateHighestPlace(trailWithElevation))).pow(2)),
                1.0 / max(1.0, ((trailIn_1_metadata[4] - trailStatsCalculator.calculateLowestPlace(trailWithElevation))).pow(2))
            )
        }

        var metaScoresAggregate = 0.0
        for (metaScore in metaScores) {
            metaScoresAggregate += metaScore
        }
        metaScoresAggregate /= 5.0

        println(metaScoresAggregate)

        val trailScore = dtwAlgorithm.runAlgorithm(trailIn_0_Id, trailIn_1)
        println(trailScore)

        return(0.6 * trailScore + 0.4 * metaScoresAggregate)
    }
}