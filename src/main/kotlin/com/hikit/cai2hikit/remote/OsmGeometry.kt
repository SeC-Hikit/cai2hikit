package com.hikit.cai2hikit.remote

data class OsmGeometry(
    val type: String,
    val coordinates: List<List<List<Double>>>
)
