package com.hikit.cai2hikit.processor

import com.hikit.cai2hikit.TrailRepository
import org.hikit.common.dto.Coordinates2D
import org.hikit.common.dto.Trail
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

@Component
class DTWAlgorithm @Autowired constructor(
    val trailRepository: TrailRepository
) {
    fun runAlgorithm(coords1: List<List<Double>>, coords2: List<List<Double>>): Double {
        val DTW = Array(coords1.size) {Array(coords2.size) {99.0}}
        DTW[0][0] = 0.0

        for(i in 1 until DTW.size) {
            for(j in 1 until DTW[i].size) {
                val cost = coordinatesDistance(coords1[i], coords2[j])
                DTW[i][j] = cost + min(min(DTW[i-1][j], DTW[i][j-1]), DTW[i-1][j-1])
            }
        }
        val result = 1.0 - min(DTW[DTW.size - 1][DTW[DTW.size - 1].size - 1], 1.0)

        return(result)
    }

    private fun coordinatesDistance(s: List<Double>, t: List<Double>): Double {
        val tempLat = (s[0] - t[0]).pow(2)
        val tempLon = (s[1] - t[1]).pow(2)
        return sqrt(tempLat + tempLon)
    }
}