package com.hikit.cai2hikit.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import java.util.*

data class Properties(
    var id: String,
    @JsonProperty("relation_id")
    @Id val relationId: Int,
    var source: String,
    @JsonProperty("cai_scale")
    var caiScale: String,
    var from: String?,
    var to: String?,
    var ref: String,
    @JsonProperty("public_page")
    var publicPage: String,
    var sda: Int,
    @JsonFormat(pattern = "Yyyy-mm-dd")
    @JsonProperty("validation_date")
    var validationDate: Date,
    @JsonFormat(pattern = "Yyyy-mm-dd HH:mm:ss")
    @JsonProperty("updated_at")
    var updatedAt: Date,
)
