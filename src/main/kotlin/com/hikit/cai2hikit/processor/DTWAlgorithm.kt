package com.hikit.cai2hikit.processor

import org.springframework.stereotype.Component
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt
import org.hikit.common.processor.Coordinates

@Component
class DTWAlgorithm {
    fun runAlgorithm(comparingTrailCoords: List<List<Double>>,
                     requestedTrailCoords: List<Coordinates>): Double {

        val dtw = Array(comparingTrailCoords.size) {Array(requestedTrailCoords.size) {99.0}}
        dtw[0][0] = 0.0

        for(i in 1 until dtw.size) {
            for(j in 1 until dtw[i].size) {
                val cost = coordinatesDistance(comparingTrailCoords[i], requestedTrailCoords[j])
                dtw[i][j] = cost + min(min(dtw[i-1][j], dtw[i][j-1]), dtw[i-1][j-1])
            }
        }
        val result = 1.0 - min(dtw[dtw.size - 1][dtw[dtw.size - 1].size - 1], 1.0)

        return(result)
    }

    private fun coordinatesDistance(s: List<Double>, t: Coordinates): Double {
        val tempLat = (s[0] - t.latitude).pow(2)
        val tempLon = (s[1] - t.longitude).pow(2)
        return sqrt(tempLat + tempLon)
    }
}