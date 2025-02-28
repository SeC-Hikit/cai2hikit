package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Coordinates2D
import com.hikit.cai2hikit.dto.Trail
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class DTWAlgorithm(
    val trailRepository: TrailRepository
) {
    private fun coordinatesDistance(s: Coordinates2D, t: Coordinates2D): Double {
        val tempLat = (s.latitude - t.latitude).pow(2)
        val tempLon = (s.longitude - t.longitude).pow(2)
        return sqrt(tempLat + tempLon)
    }

    fun runAlgorithm(trailId: String, trailIn_1: Trail): Double {
        val trailIn_0 = trailRepository.findByPropsId(trailId)
        var DTW = Array(trailIn_0!!.geometry.coordinates[0].size) {Array(trailIn_1.geometry.coordinates[0].size) {99.0}}
        DTW[0][0] = 0.0

        for(i in 1 until DTW.size) {
            for(j in 1 until DTW[i].size) {
                val cost = coordinatesDistance(trailIn_0.geometry.coordinates[0][i], trailIn_1.geometry.coordinates[0][j])
                DTW[i][j] = cost + min(min(DTW[i-1][j], DTW[i][j-1]), DTW[i-1][j-1])
            }
        }
        print(DTW[DTW.size - 1][DTW[DTW.size - 1].size - 1].toString() + " ")
        return(DTW[DTW.size - 1][DTW[DTW.size - 1].size - 1])
    }
}