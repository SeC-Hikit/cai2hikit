package com.hikit.cai2hikit.processor

import org.springframework.stereotype.Component
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import org.hikit.common.processor.Coordinates

@Component
class DTWAlgorithm {
    fun run(
        comparingTrailCoords: List<Coordinates>,
        requestedTrailCoords: List<Coordinates>
    ): Double {

        val dtw = Array(comparingTrailCoords.size) { Array(requestedTrailCoords.size) { 99.0 } }
        dtw[0][0] = 0.0

        for (i in 1 until dtw.size) {
            for (j in 1 until dtw[i].size) {
                val cost = coordinatesDistance(comparingTrailCoords[i], requestedTrailCoords[j])
                dtw[i][j] = cost + min(min(dtw[i - 1][j], dtw[i][j - 1]), dtw[i - 1][j - 1])
            }
        }

        return 1.0 - min(dtw[dtw.size - 1][dtw[dtw.size - 1].size - 1], 1.0)
    }

    private fun coordinatesDistance(comparingCoordinate: Coordinates, requestCoordinate: Coordinates): Double {
        val tempLat = (comparingCoordinate.latitude - requestCoordinate.latitude).pow(2)
        val tempLon = (comparingCoordinate.longitude - requestCoordinate.longitude).pow(2)
        return sqrt(tempLat + tempLon)
    }
}