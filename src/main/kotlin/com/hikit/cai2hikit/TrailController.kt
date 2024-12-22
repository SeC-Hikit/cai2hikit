package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Trail
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/v1/trail")
class TrailController {
    @GetMapping("/{code}")
    fun getTrail(@PathVariable code: Int): Trail? {
        return null
    }
}