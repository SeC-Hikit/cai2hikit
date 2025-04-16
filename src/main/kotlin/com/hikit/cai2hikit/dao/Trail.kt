package com.hikit.cai2hikit.dao

import org.bson.types.ObjectId

class Trail (
    var id: ObjectId = ObjectId(),
    var properties: Properties = Properties(),
    var geometry: Geometry = Geometry()
) {
}