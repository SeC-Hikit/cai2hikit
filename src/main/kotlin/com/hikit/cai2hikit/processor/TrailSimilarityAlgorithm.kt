package com.hikit.cai2hikit.processor

import com.hikit.cai2hikit.TrailRepository
import com.hikit.cai2hikit.adapter.AltitudeServiceWrapper
import org.hikit.common.dto.MatchingRequest
import org.hikit.common.processor.Coordinates
import org.hikit.common.processor.TrailsStatsCalculator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.math.max
import kotlin.math.pow


@Component
class TrailSimilarityAlgorithm @Autowired constructor(
    private val trailRepository: TrailRepository,
    private val dtwAlgorithm: DTWAlgorithm,
    private val altitudeServiceAdapter: AltitudeServiceWrapper,
    private val trailStatsCalculator: TrailsStatsCalculator
) {
    fun run(requestData: MatchingRequest): Pair<String, Double> {

        // 1st: take the edge coordinates and geo filter -> intersects: listOf(Trail)

        // 2nd: loop through found trails and calculate score

        var bestMatchResult: Pair<String, Double> = Pair("", 0.0)

        for (trail in trailRepository.findAll()) {
            val trailWithoutElevation: List<List<Double>> = trail!!.geometry.coordinates
            val trailWithElevation: List<Coordinates> = altitudeServiceAdapter.mapCoordsWithElevations(trailWithoutElevation)

//            val trailScore = dtwAlgorithm.runAlgorithm(trailWithoutElevation, requestData.geometry.geoline.coordinates)
//            TODO: reconvert object to DTO
            val trailScore = dtwAlgorithm.runAlgorithm(trailWithoutElevation, emptyList())

            val metaScoresAggregate = computeMetaScores(requestData, trailWithElevation)
            val finalScore = (0.6 * trailScore + 0.4 * metaScoresAggregate)

            if (finalScore > bestMatchResult.second) {
                bestMatchResult = Pair(trail.properties.id, finalScore)
            }
        }
        return bestMatchResult
    }

    fun computeMetaScores(requestData: MatchingRequest, trailIn: List<Coordinates>): Double {
        val metaScores: Array<Double> = arrayOf(
            1.0 / max(1.0, ((requestData.metadata.totalRise - trailStatsCalculator.calculateTotRise(trailIn))).pow(2)),
            1.0 / max(1.0, ((requestData.metadata.totalFall - trailStatsCalculator.calculateTotFall(trailIn))).pow(2)),
            1.0 / max(1.0, ((requestData.metadata.length - trailStatsCalculator.calculateTrailLength(trailIn))).pow(2)),
            1.0 / max(1.0, ((requestData.metadata.highest - trailStatsCalculator.calculateHighestPlace(trailIn))).pow(2)),
            1.0 / max(1.0, ((requestData.metadata.lowest - trailStatsCalculator.calculateLowestPlace(trailIn))).pow(2))
        )

        var metaScoresAggregate = 0.0
        for (metaScore in metaScores) {
            metaScoresAggregate += metaScore
        }
        metaScoresAggregate /= 5.0

        return(metaScoresAggregate)
    }
}