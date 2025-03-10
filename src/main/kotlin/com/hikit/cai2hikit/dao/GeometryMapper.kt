package com.hikit.cai2hikit.dao

import org.springframework.stereotype.Component

@Component
class GeometryMapper {
    fun map(toMap: org.hikit.common.dto.Geometry): Geometry {
        return Geometry("LineString",
            toMap.coordinates.flatMap {
                it.map { coords -> listOf(coords.longitude, coords.latitude) }
            })
    }
}