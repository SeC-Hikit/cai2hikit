package com.hikit.cai2hikit

import org.hikit.common.dto.Trail
import com.hikit.cai2hikit.exception.NotFoundException
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/trail")
class TrailController(
    val trailRepository: TrailRepository
) {

    @GetMapping("/code/{code}")
    fun getTrail(@PathVariable code: String): Trail? {
        val findByPropsId = trailRepository.findByPropsId(code)
            ?: throw NotFoundException("Could not find any trail matching code (id)=${code}")
        return findByPropsId
    }

    @GetMapping("/ref/{ref}")
    fun getTrailByRef(@PathVariable ref: String): List<Trail>? {
        val findByRef = trailRepository.findByRef(ref)
        if (findByRef.isEmpty()) {
            throw NotFoundException("Could not find any trail matching refCode=${ref}")
        }
        return findByRef
    }
}