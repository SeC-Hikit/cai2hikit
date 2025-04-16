package com.hikit.cai2hikit.dao

import java.util.*

data class Properties(
    var id: String = "",
    val relationId: Int = 0,
    var source: String? = "",
    var caiScale: String = "",
    var from: String? = "",
    var to: String? = "",
    var ref: String? = "",
    var publicPage: String = "",
    var sda: Int = 0,
    var validationDate: Date = Date(),
    var updatedAt: Date = Date(),
)