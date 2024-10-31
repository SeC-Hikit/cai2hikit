package com.hikit.cai2hikit.caidata

import org.springframework.data.annotation.Id
import org.bson.types.ObjectId
import java.util.*

class Trail {
    @Id private var id: ObjectId = ObjectId()

    private var relation_id: Int = 0
    private var source: String = ""
    private var cai_scale: String = ""
    private var from: String = ""
    private var to: String = ""
    private var ref: String = ""
    private var public_page: String = ""
    private var sda: Int = 0
    private var validation_date: Date = Date()
    private var updated_at: Date = Date()

    private var coordinates: Array<Coordinates2D> = emptyArray()

    fun getId(): String {
        return id.toString()
    }

    fun setId(id: Int) {
        val date = Date()
        this.id = ObjectId(date, id)
    }

    fun getRelationId(): Int {
        return relation_id
    }

    fun setRelationId(relation_id: Int) {
        this.relation_id = relation_id
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

    fun getPublicPage(): String {
        return public_page
    }

    fun setPublicPage(public_page: String) {
        this.public_page = public_page
    }

    fun getSda(): Int {
        return sda
    }

    fun setSda(sda: Int) {
        this.sda = sda
    }

    fun getValidation_date(): Date {
        return validation_date
    }

    fun setValidationDate(validation_date: Date) {
        this.validation_date = validation_date
    }

    fun getUpdated_at(): Date {
        return updated_at
    }

    fun setUpdatedAt(updated_at: Date) {
        this.updated_at = updated_at
    }

    fun getCoordinates(): Array<Coordinates2D> {
        return coordinates
    }

    fun setCoordinates(coordinates: Array<Coordinates2D>) {
        this.coordinates = coordinates
    }
}