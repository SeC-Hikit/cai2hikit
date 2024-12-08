package com.hikit.cai2hikit.dto

class Coordinates2D public constructor(coordinates: Array<Double>) {
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    init {
        longitude = coordinates[0]
        latitude = coordinates[1]
    }
}