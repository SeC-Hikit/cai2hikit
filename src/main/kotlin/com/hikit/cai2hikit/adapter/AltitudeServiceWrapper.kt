package com.hikit.cai2hikit.adapter

import org.hikit.common.dto.Coordinates2D
import com.hikit.cai2hikit.processor.Coordinates
import org.hikit.common.adapter.AltitudeServiceAdapter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class AltitudeServiceWrapper @Autowired constructor(
    private val altitudeServiceAdapter: AltitudeServiceAdapter
) {
    fun mapCoordsWithElevations(coordinates: List<Coordinates2D>): List<Coordinates> =
        altitudeServiceAdapter.getElevationsByLongLat(coordinates.map { Pair(it.longitude, it.latitude) })
            .mapIndexed{ index, altitude ->
                Coordinates(
                    coordinates[index].longitude,
                    coordinates[index].latitude, altitude
                )
            }

    fun getElevationsByLongLat(latitude: Double, longitude: Double) = altitudeServiceAdapter.getElevationsByLongLat(latitude, longitude)
    fun getElevationsByLongLat(coordinates: List<Pair<Double, Double>>) = altitudeServiceAdapter.getElevationsByLongLat(coordinates)
}