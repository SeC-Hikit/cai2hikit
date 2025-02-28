package com.hikit.cai2hikit.processor

import com.hikit.cai2hikit.TrailRepository
import com.hikit.cai2hikit.dto.Trail

class TrailSimilarityAlgorithm(
    val trailRepository: TrailRepository
) {
    fun runAlgorithm(trailIn_0_Id: String, trailIn_1: Trail): Double {
        val trailIn_0 = trailRepository.findByPropsId(trailIn_0_Id)
        var scores = arrayOf(0.0, 0.0, 0.0, 0.0, 0.0)

        return 1.0
    }
}