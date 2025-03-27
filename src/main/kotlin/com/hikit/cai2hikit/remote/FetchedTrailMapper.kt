package com.hikit.cai2hikit.remote

import com.hikit.cai2hikit.dao.Geometry
import com.hikit.cai2hikit.dao.Properties
import com.hikit.cai2hikit.dao.Trail
import org.hikit.common.dto.Coordinates2D
import org.springframework.stereotype.Component

@Component
class FetchedTrailMapper {
    fun dtoToCoords2D(toMap: Geometry): List<Coordinates2D> {
        return toMap.coordinates.map {
            Coordinates2D(it[0], it[1])
        }
    }

    fun mapToEntity(toMap: OsmTrail): Trail {
        return Trail(
            Properties(
                toMap.properties.id,
                toMap.properties.relationId,
                toMap.properties.source,
                toMap.properties.caiScale,
                toMap.properties.from,
                toMap.properties.to,
                toMap.properties.ref,
                toMap.properties.publicPage,
                toMap.properties.sda,
                toMap.properties.validationDate,
                toMap.properties.updatedAt,
            ),
            Geometry("LineString", toMap.geometry.coordinates.first())
        )
    }

}