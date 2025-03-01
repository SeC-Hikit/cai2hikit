package com.hikit.cai2hikit.processor

import com.hikit.cai2hikit.TrailRepository
import com.hikit.cai2hikit.dto.Coordinates2D
import com.hikit.cai2hikit.dto.Trail
import org.hikit.common.adapter.AltitudeServiceAdapter
import org.hikit.common.processor.Coordinates
import org.hikit.common.processor.TrailsStatsCalculator


class TrailSimilarityAlgorithm(
    val trailRepository: TrailRepository,
    val trailsCalculator: TrailsStatsCalculator,
    val altitudeServiceAdapter: AltitudeServiceAdapter,
    val dtwAlgorithm: DTWAlgorithm
) {
    fun runAlgorithm(trailIn_0_Id: String, trailIn_1: Trail): Double {
        val trailIn_0 = trailRepository.findByPropsId(trailIn_0_Id)
        var scores = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0)

        if (trailIn_0 != null) {
            val trailWithoutElevation: List<Coordinates2D> = trailIn_0.geometry.coordinates[0]
            var trailWithElevation: List<Coordinates> = altitudeServiceAdapter.getElevationsByLongLat(trailWithoutElevation.map {
                Pair(it.latitude, it.longitude) }).mapIndexed(
                {
                    index,
                    altitude -> Coordinates(trailWithoutElevation[index].latitude,
                trailWithoutElevation[index].longitude, altitude)
                }
            )
            println(trailWithElevation[0])
        }


        val trailScore = dtwAlgorithm.runAlgorithm(trailIn_0_Id, trailIn_1)

        return 1.0
    }
}