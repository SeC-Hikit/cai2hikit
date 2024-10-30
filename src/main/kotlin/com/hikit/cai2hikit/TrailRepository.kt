package com.hikit.cai2hikit

import com.hikit.cai2hikit.caidata.Trail
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.repository.query.Param
import org.springframework.data.rest.core.annotation.RepositoryRestResource

@RepositoryRestResource(collectionResourceRel = "trails", path = "trails")
interface TrailRepository : MongoRepository<Trail?, String?> {
    fun findByRef(@Param("ref") name: String): kotlin.collections.List<Trail?>?
}