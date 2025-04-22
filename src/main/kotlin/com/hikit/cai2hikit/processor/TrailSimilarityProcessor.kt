package com.hikit.cai2hikit.processor

import com.hikit.cai2hikit.GeoTrailRepository
import com.hikit.cai2hikit.adapter.AltitudeServiceWrapper
import com.hikit.cai2hikit.dao.Trail
import com.hikit.cai2hikit.remote.FetchedTrailMapper
import org.hikit.common.dto.CoordinatesDto
import org.hikit.common.dto.MatchingRequest
import org.hikit.common.geo.CoordinatesRectangle
import org.hikit.common.processor.Coordinates
import org.hikit.common.processor.TrailsStatsCalculator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.pow


private const val matchingScoreWeight = 0.6
private const val metaScoreWeight = 0.4
private const val metaScoresNumber = 5.0

@Component
class TrailSimilarityProcessor @Autowired constructor(
    private val dtwAlgorithm: DTWAlgorithm,
    private val altitudeServiceAdapter: AltitudeServiceWrapper,
    private val trailStatsCalculator: TrailsStatsCalculator,
    private val geoTrailRepository: GeoTrailRepository,
    private val fetchedTrailMapper: FetchedTrailMapper,
) {
    fun run(requestData: MatchingRequest): List<Pair<Trail, Double>> {
        // 1st: take the edge coordinates and geo filter -> intersects: listOf(Trail)
        val foundByIntersecting = geoTrailRepository.findByIntersection(
            getOuterSquareForCoordinates(requestData.coordinates)
        )

        val requestCoords = requestData.coordinates.map {
            Coordinates(
                longitude = it.longitude,
                latitude = it.latitude,
                altitude = it.altitude
            )
        }

        return foundByIntersecting.map {
            val intersectedTrailCoord: List<Coordinates> =
                altitudeServiceAdapter.mapCoordsWithElevations(fetchedTrailMapper.dtoToCoords2D(it.geometry))
            val trailScore = dtwAlgorithm.run(requestCoords, intersectedTrailCoord)
            val metaScoresAggregate = computeMetaScores(requestData, intersectedTrailCoord)
            val finalScore = (matchingScoreWeight * trailScore + metaScoreWeight * metaScoresAggregate)
            Pair(it, floor(finalScore * 100) / 100f)
        }.sortedByDescending { it.second }
    }

    fun computeMetaScores(requestData: MatchingRequest, trailIn: List<Coordinates>): Double {
        val metaScores: Array<Double> = arrayOf(
            1.0 / max(1.0, ((requestData.metadata.totalRise - trailStatsCalculator.calculateTotRise(trailIn))).pow(2)),
            1.0 / max(1.0, ((requestData.metadata.totalFall - trailStatsCalculator.calculateTotFall(trailIn))).pow(2)),
            1.0 / max(1.0, ((requestData.metadata.length - trailStatsCalculator.calculateTrailLength(trailIn))).pow(2)),
            1.0 / max(1.0, ((requestData.metadata.highest - trailStatsCalculator.calculateHighestPlace(trailIn))).pow(2)),
            1.0 / max(1.0, ((requestData.metadata.lowest - trailStatsCalculator.calculateLowestPlace(trailIn))).pow(2))
        )

        // calculate average
        val metaScoresAggregate = metaScores.sum() / metaScoresNumber

        return (metaScoresAggregate)
    }

    fun getOuterSquareForCoordinates(
        coordinates2D: List<CoordinatesDto>,
        paddingDistance: Double = 0.0
    ): CoordinatesRectangle {
        val topRight = Coordinates(coordinates2D.maxOf { it.longitude + paddingDistance },
            coordinates2D.maxOf { it.latitude + paddingDistance })
        val bottomLeft = Coordinates(coordinates2D.minOf { it.longitude - paddingDistance },
            coordinates2D.minOf { it.latitude - paddingDistance })
        return CoordinatesRectangle(bottomLeft, topRight)
    }

}