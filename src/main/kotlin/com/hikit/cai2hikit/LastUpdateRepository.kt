package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.IdToUpdateDate
import org.springframework.data.mongodb.repository.MongoRepository

interface LastUpdateRepository : MongoRepository<IdToUpdateDate, Int> {

}