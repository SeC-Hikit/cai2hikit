package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Trail
import com.hikit.cai2hikit.exception.NotFoundException
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.NoHandlerFoundException


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