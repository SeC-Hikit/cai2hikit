package com.hikit.cai2hikit.dao

import org.hikit.common.dto.Coordinates2D
import org.springframework.stereotype.Component

@Component
class DaoTrailMapper {

    fun mapToDto(trail: Trail) : org.hikit.common.dto.Trail {
        val properties = trail.properties
        return org.hikit.common.dto.Trail(
            properties = org.hikit.common.dto.Properties(
                id = properties.id,
                relationId = properties.relationId,
                source = properties.source,
                caiScale = properties.caiScale,
                from = properties.from,
                to = properties.to,
                ref = properties.ref,
                publicPage = properties.publicPage,
                sda = properties.sda,
                validationDate = properties.validationDate,
                updatedAt = properties.updatedAt
            ),
            geometry = org.hikit.common.dto.Geometry(
                type = trail.geometry.type,
                coordinates = trail.geometry.coordinates.map {
                    Coordinates2D(it[1], it[0])
                }
            )
        )
    }
}