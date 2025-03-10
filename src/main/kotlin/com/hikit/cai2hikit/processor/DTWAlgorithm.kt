package com.hikit.cai2hikit.processor

import com.hikit.cai2hikit.TrailRepository
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
        val dtw = Array(coords1.size) {Array(coords2.size) {99.0}}
        dtw[0][0] = 0.0

        for(i in 1 until dtw.size) {
            for(j in 1 until dtw[i].size) {
                val cost = coordinatesDistance(coords1[i], coords2[j])
                dtw[i][j] = cost + min(min(dtw[i-1][j], dtw[i][j-1]), dtw[i-1][j-1])
            }
        }
        val result = 1.0 - min(dtw[dtw.size - 1][dtw[dtw.size - 1].size - 1], 1.0)

        return(result)
    }

    private fun coordinatesDistance(s: List<Double>, t: List<Double>): Double {
        val tempLat = (s[0] - t[0]).pow(2)
        val tempLon = (s[1] - t[1]).pow(2)
        return sqrt(tempLat + tempLon)
    }
}