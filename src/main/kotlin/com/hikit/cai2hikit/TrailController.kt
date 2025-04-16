package com.hikit.cai2hikit

import com.hikit.cai2hikit.dao.DaoTrailMapper
import com.hikit.cai2hikit.exception.NotFoundException
import com.hikit.cai2hikit.processor.TrailSimilarityProcessor
import org.hikit.common.dto.MatchingRequest
import org.hikit.common.dto.Trail
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/v1/trail")
class TrailController(
    val trailRepository: TrailRepository,
    val trailMapper: DaoTrailMapper,
    val trailSimilarityProcessor: TrailSimilarityProcessor
) {

    @GetMapping("/code/{code}")
    fun getTrail(@PathVariable code: String): Trail? {
        val findByPropsId = trailRepository.findByPropsId(code)
            ?: throw NotFoundException("Could not find any trail matching code (id)=${code}")

        return trailMapper.mapToDto(findByPropsId)
    }

    @GetMapping("/ref/{ref}")
    fun getTrailByRef(@PathVariable ref: String): List<Trail>? {
        val findByRef = trailRepository.findByRef(ref)
        if (findByRef.isEmpty()) {
            throw NotFoundException("Could not find any trail matching refCode=${ref}")
        }
        return findByRef.map { trailMapper.mapToDto(it) }
    }

    @PostMapping("/match")
    fun matchTrail(@RequestBody req: MatchingRequest): List<Trail>? {
        val matchingTrail = trailSimilarityProcessor.run(req)
        if (matchingTrail.isEmpty()) {
            throw NotFoundException("Could not find any trail matching the request")
        }
        val toTake = if (matchingTrail.size > 10) 10 else matchingTrail.size
        return matchingTrail.map { trailMapper.mapToDto(it.first) }.take(toTake)
    }
}