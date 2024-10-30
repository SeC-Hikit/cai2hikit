package com.hikit.cai2hikit.caidata

import org.springframework.data.annotation.Id
import org.bson.types.ObjectId
import java.util.*

class Trail {
    @Id private var id: ObjectId = ObjectId()

    private var source: String = ""
    private var cai_scale: String = ""
    private var from: String = ""
    private var to: String = ""
    private var ref: String = ""
    private var sda: Int = 0
    private var coordinates: Array<Coordinates2D> = emptyArray()

    fun setId(idIn: String) {
        val date = Date()
        id = ObjectId(date, idIn.toInt())
    }

    fun getId(): String {
        return id.toString()
    }

    fun getSource(): String {
        return source
    }

    fun setSource(source: String) {
        this.source = source
    }

    fun getCai_scale(): String {
        return cai_scale
    }

    fun setCai_scale(cai_scale: String) {
        this.cai_scale = cai_scale
    }

    fun getFrom(): String {
        return from
    }

    fun setFrom(from: String) {
        this.from = from
    }

    fun getTo(): String {
        return to
    }

    fun setTo(to: String) {
        this.to = to
    }

    fun getRef(): String {
        return ref
    }

    fun setRef(ref: String) {
        this.ref = ref
    }

    fun getSda(): Int {
        return sda
    }

    fun setSda(sda: Int) {
        this.sda = sda
    }

    fun getCoordinates(): Array<Coordinates2D> {
        return coordinates
    }

    fun setCoordinates(coordinates: Array<Coordinates2D>) {
        this.coordinates = coordinates
    }
}