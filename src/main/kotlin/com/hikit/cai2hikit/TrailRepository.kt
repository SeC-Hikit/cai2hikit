package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Trail
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "trails", path = "trails")
interface TrailRepository : MongoRepository<Trail?, Int?> {
    @Query(value = "{ 'properties.id' : ?0 }")
    fun findByPropsId(id: String) : Trail?
}
