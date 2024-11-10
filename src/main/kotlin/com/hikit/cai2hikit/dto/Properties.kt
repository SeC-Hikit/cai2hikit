package com.hikit.cai2hikit.dto

import com.fasterxml.jackson.annotation.JsonFormat
import org.springframework.data.annotation.Id
import java.util.*

data class Properties(
    var id: String,
//    @Id val relation_id: Int,
    var source: String,
    var cai_scale: String,
    var from: String,
    var to: String,
    var ref: String,
    var public_page: String,
    var sda: Int,
//    @JsonFormat(pattern = "Yyyy-mm-dd")
//    var validation_date: Date,
//    @JsonFormat(pattern = "Yyyy-mm-dd HH:mm:ss")
//    var updated_at: Date,
)
