package com.hikit.cai2hikit.conf

import com.mongodb.client.model.Indexes
import jakarta.annotation.PostConstruct
import org.hikit.common.datasource.Datasource
import org.springframework.context.annotation.Configuration

const val trailCollection = "trail"
private const val geometryField = "geometry"

@Configuration
class DbInitConfiguration(val dataSource: Datasource) {


    @PostConstruct
    fun init() =
        dataSource.db.getCollection(trailCollection)
            .createIndex(Indexes.geo2dsphere(geometryField))
}
