package com.hikit.cai2hikit.dao

import org.hikit.common.dto.Coordinates2D
import org.springframework.stereotype.Component

@Component
class GeometryMapper {
    fun listToCoords2D(toMap: List<Double>): List<Coordinates2D> {
        return
    }

    fun dtoToCoords2D(toMap: org.hikit.common.dto.Geometry): List<Coordinates2D> {
        return toMap.coordinates.map {
            Coordinates2D(it[0], it[1])
        }
    }

    fun mapToData(toMap: org.hikit.common.dto.Geometry): Geometry {
        return Geometry("LineString",
            toMap.coordinates)
    }

    fun mapToDto(toMap: Geometry): org.hikit.common.dto.Geometry {
        return org.hikit.common.dto.Geometry("MultiLineString",
            toMap.coordinates)
    }
}