package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Trail
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/trail")
class TrailController(
    val trailRepository: TrailRepository
) {
    @GetMapping("/code/{code}")
    fun getTrail(@PathVariable code: String): Trail? {
        return(trailRepository.findByPropsId(code))
    }

    @GetMapping("/ref/{ref}")
    fun getTrailByRef(@PathVariable ref: String): List<Trail>? {
        return(trailRepository.findByRef(ref))
    }
}