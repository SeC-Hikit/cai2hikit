package com.hikit.cai2hikit.processor

import org.hikit.common.processor.Coordinates

data class Coordinates(
    private val longitude: Double,
    private val latitude: Double,
    private val altitude: Double
) : Coordinates {
    override fun getLongitude() = longitude
    override fun getLatitude() = latitude
    override fun getAltitude() = altitude
}
