package com.hikit.cai2hikit.controller

import com.hikit.cai2hikit.dto.CoordinatesDto
import com.hikit.cai2hikit.dto.Coordinates2D
import io.swagger.v3.oas.annotations.Operation
import org.sc.data.validator.GeneralValidator
import org.sc.manager.GeoToolManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import java.util.stream.Collectors

@RestController
@RequestMapping(GeoToolsController.PREFIX)
class GeoToolsController @Autowired constructor(
//    generalValidator: GeneralValidator,
    geoToolManager: GeoToolManager
) {
//    private val generalValidator: GeneralValidator = generalValidator
    private val geoToolManager: GeoToolManager = geoToolManager

    @Operation(summary = "Find a point elevation by lat-long")
    @GetMapping("/altitude")
    fun geoLocateTrail(
        @RequestParam latitude: Double,
        @RequestParam longitude: Double
    ): CoordinatesDto {
/*        val errors: Set<String> = generalValidator.validate(Coordinates(latitude, longitude))
        if (!errors.isEmpty()) {
            return Coordinates()
        } */
        val altitudeByLongLat: Double = geoToolManager
            .getAltitudeByLongLat(latitude, longitude).stream().findFirst()
            .orElseThrow { RuntimeException() }
        return CoordinatesDto(latitude, longitude, altitudeByLongLat)
    }

    @Operation(summary = "Find polyline point elevations")
    @GetMapping("/polyline_altitude")
    fun getAltitudeTrail(@RequestBody coordinatesDtoList: List<Coordinates2D>): List<CoordinatesDto> {
/*        val errors = coordinatesDtoList.stream().map<Any>(generalValidator::validate)
            .flatMap<Any> { obj: Any -> obj.stream() }.collect(Collectors.toSet<Any>())
        if (!errors.isEmpty()) {
            return emptyList<Coordinates>()
        } */
        return coordinatesDtoList.stream().map<Any> { it: Coordinates2D ->
            geoToolManager.getCoordinateByLongLat(
                it.latitude,
                it.longitude
            )
        }.collect(Collectors.toList<Any>())
    }

    @Operation(summary = "Find coordinates distance")
    @PostMapping("/distance")
    fun radialDistance(@RequestBody coordinatesDtoList: List<CoordinatesDto?>): Double {
/*        val errors = coordinatesDtoList.stream().map<Any>(generalValidator::validate)
            .flatMap<Any> { obj: Any -> obj.stream() }.collect(Collectors.toSet<Any>())
        if (!errors.isEmpty()) {
            return 0.0
        } */
        return geoToolManager.getDistanceBetweenCoordinates(coordinatesDtoList)
    }

    companion object {
        const val PREFIX: String = "/geo-tool"
    }
}
