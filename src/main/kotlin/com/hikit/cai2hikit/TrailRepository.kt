package com.hikit.cai2hikit

import org.hikit.common.dto.Trail
import org.bson.Document
import org.hikit.common.datasource.MongoUtils.*
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.rest.core.annotation.RepositoryRestResource
import java.util.*


@RepositoryRestResource(collectionResourceRel = "trails", path = "trails")
interface TrailRepository : MongoRepository<Trail?, Int?> {
    @Query(value = "{ 'properties.id' : ?0 }")
    fun findByPropsId(id: String) : Trail?

    @Query(value = "{ 'properties.ref': ?0 }")
    fun findByRef(ref: String) : List<Trail>
}
