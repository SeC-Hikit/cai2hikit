package com.hikit.cai2hikit.dto

class Coordinates2D {
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    public constructor(coordinates: Array<Double>) {
        longitude = coordinates[0]
        latitude = coordinates[1]
    }
}