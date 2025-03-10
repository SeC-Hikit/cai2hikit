package com.hikit.cai2hikit

import com.hikit.cai2hikit.conf.trailCollection
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.hikit.common.datasource.Datasource
import org.hikit.common.datasource.MongoUtils.*
import org.hikit.common.dto.Trail
import org.hikit.common.geo.CoordinatesRectangle
import org.hikit.common.processor.Coordinates
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class GeoTrailRepository @Autowired constructor(dataSource: Datasource) {

    private val collection: MongoCollection<Trail> = dataSource.db.getCollection(trailCollection, Trail::class.java)

    fun findByIntersection(geoSquare: CoordinatesRectangle): List<Trail> {
        val polygonCoords = getCoords(geoSquare)
        return collection.find(
            Document(
                "geometry",
                Document(
                    `$_GEO_INTERSECT`,
                    Document(
                        `$_GEOMETRY`, Document(GEO_TYPE, GEO_POLYGON)
                            .append(
                                GEO_COORDINATES,
                                listOf(
                                    polygonCoords
                                )
                            )
                    )
                )
            )
        ).toList()
    }

    private fun getCoords(geoSquare: CoordinatesRectangle): List<List<Double>> {
        val resolvedTopLeftVertex = resolveVertex(geoSquare.bottomLeft, geoSquare.topRight)
        val resolvedBottomRightVertex = resolveVertex(geoSquare.topRight, geoSquare.bottomLeft)
        val bottomLeft = listOf(geoSquare.bottomLeft.longitude, geoSquare.bottomLeft.latitude)
        val polygonCoords = listOf(
            bottomLeft,
            resolvedTopLeftVertex,
            listOf(geoSquare.topRight.longitude, geoSquare.topRight.latitude),
            resolvedBottomRightVertex,
            bottomLeft
        )
        return polygonCoords
    }

    private fun resolveVertex(bottomLeft: Coordinates, topRight: Coordinates): List<Double> {
        return listOf(
            bottomLeft.longitude,
            topRight.latitude
        )
    }
}