package com.hikit.cai2hikit.dao

import org.bson.Document
import org.springframework.stereotype.Component


@Component
class TrailMapper {

    fun map(document : Document) : Trail {
        val properties = document.get("properties", Document::class.java)
        val geometry = document.get("geometry", Document::class.java)
        val coordLists = geometry.get("coordinates", List::class.java)
        val filterIsInstance = coordLists.filterIsInstance<List<Double>>()
        return Trail(
            id = document.getObjectId("_id"),
            properties = Properties(
                id = properties.getString("_id"),
                relationId = properties.getInteger("relationId"),
                source = properties.getString("source"),
                caiScale = properties.getString("caiScale"),
                from = properties.getString("from"),
                to = properties.getString("to"),
                ref = properties.getString("ref"),
                publicPage = properties.getString("publicPage"),
                sda = properties.getInteger("sda"),
                validationDate = properties.getDate("validationDate"),
                updatedAt = properties.getDate("updatedAt"),
            ),

            geometry = Geometry(
                type = geometry.getString("type"),
                coordinates = filterIsInstance
            )

        )
    }
}