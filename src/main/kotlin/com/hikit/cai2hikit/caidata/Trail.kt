package com.hikit.cai2hikit.caidata

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.Id
import org.bson.types.ObjectId
import java.util.*

data class Trail(
    val id: String,
    @Id val relation_id: Int,
    val source: String,
    val cai_scale: String,
    val from: String,
    val to: String,
    val ref: String,
    val public_page: String,
    val sda: Int,
    @JsonFormat(pattern = "Yyyy-mm-dd")
    val validation_date: Date,
    @JsonFormat (pattern = "Yyyy-mm-dd HH:mm:ss")
    val updated_at: Date,
    val coordinates: Array<Coordinates2D>
)
