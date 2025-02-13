package com.hikit.cai2hikit.dto

import com.hikit.cai2hikit.dao.Coordinates

class CoordinatesDto : Coordinates {

    constructor() {
        this.longitude = 0.0
        this.latitude = 0.0
        this.altitude = 0.0
    }

    constructor(latitude: Double, longitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
        this.altitude = 0.0
    }

    constructor(latitude: Double, longitude: Double, altitude: Double) {
        this.latitude = latitude
        this.longitude = longitude
        this.altitude = altitude
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as CoordinatesDto
        return java.lang.Double.compare(that.latitude, latitude) == 0 && java.lang.Double.compare(
            that.longitude,
            longitude
        ) == 0 && java.lang.Double.compare(that.altitude, altitude) == 0
    }
}