package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Coordinates2D
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class DTWAlgorithm {
    val s1: List<List<Coordinates2D>> = listOf(
        listOf(
            Coordinates2D(
                11.5473039,
                43.9653826
            ),
            Coordinates2D(
                11.5478117,
                43.9657346
            ),
            Coordinates2D(
                11.5481934,
                43.9659758
            ),
            Coordinates2D(
                11.5484466,
                43.9661336
            )
        )
    )
    val t1: List<List<Coordinates2D>> = listOf(
        listOf(
            Coordinates2D(
                11.5473039,
                43.9653826
            ),
            Coordinates2D(
                11.5478117,
                43.9657346
            ),
            Coordinates2D(
                11.5481934,
                43.9659758
            ),
            Coordinates2D(
                11.5484466,
                43.9661336
            )
        )
    )

    fun coordinatesDistance(s: Coordinates2D, t: Coordinates2D): Double {
        val tempLat = (s.latitude - t.latitude).pow(2)
        val tempLon = (s.longitude - t.longitude).pow(2)
        return sqrt(tempLat + tempLon)
    }

    fun getDistanceValue(s: List<List<Coordinates2D>>, t: List<List<Coordinates2D>>) {
        var DTW = Array(s[0].size) {Array(t[0].size) {99.0}}
        DTW[0][0] = 0.0

        for(i in 1 until DTW.size) {
            for(j in 1 until DTW[i].size) {
                val cost = coordinatesDistance(s[0][i], t[0][j])
                DTW[i][j] = cost + min(min(DTW[i-1][j], DTW[i][j-1]), DTW[i-1][j-1])
            }
        }
        print(DTW[DTW.size - 1][DTW[DTW.size - 1].size - 1].toString() + " ")
    }
}