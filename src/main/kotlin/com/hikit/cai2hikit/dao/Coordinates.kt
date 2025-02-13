package com.hikit.cai2hikit.dao

interface Coordinates {
    var longitude: Double
    var latitude: Double
    var altitude: Double

    companion object {
        const val COORDINATES: String = "coordinates"
        const val GEO_TYPE: String = "type"
        const val ALTITUDE: String = "altitude"
    }
}
