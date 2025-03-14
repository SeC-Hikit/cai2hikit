package com.hikit.cai2hikit.dao

import org.hikit.common.dto.Coordinates2D
import org.springframework.stereotype.Component

@Component
class GeometryMapper {
    fun mapToData(toMap: org.hikit.common.dto.Geometry): Geometry {
        return Geometry("LineString",
            toMap.coordinates.flatMap {
                it.map { coords -> listOf(coords.longitude, coords.latitude) }
            })
    }

    fun mapToDto(toMap: Geometry): org.hikit.common.dto.Geometry {
        return org.hikit.common.dto.Geometry("MultiLineString",
            listOf (
                toMap.coordinates.map { Coordinates2D(it[0], it[1]) }
            ))
    }
}