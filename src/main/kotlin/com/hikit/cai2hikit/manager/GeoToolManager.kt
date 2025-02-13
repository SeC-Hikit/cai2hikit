package com.hikit.cai2hikit.manager

import com.hikit.cai2hikit.dto.CoordinatesDto
import com.hikit.cai2hikit.processor.TrailsStatsCalculator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GeoToolManager @Autowired constructor(private val altitudeService : AltitudeServiceAdapter,
                                            private val trailsStatsCalculator: TrailsStatsCalculator
) {

    fun getAltitudeByLongLat(latitude: Double, longitude: Double) =
        altitudeService.getElevationsByLongLat(latitude, longitude)

    fun getCoordinateByLongLat(latitude: Double, longitude: Double): CoordinatesDto =
        CoordinatesDto(latitude, longitude, altitudeService.getElevationsByLongLat(latitude, longitude).firstNotNullOf { 0.0 })

    fun getDistanceBetweenCoordinates(coords: List<CoordinatesDto>) : Double =
        trailsStatsCalculator.calculateTrailLength(coords)
}