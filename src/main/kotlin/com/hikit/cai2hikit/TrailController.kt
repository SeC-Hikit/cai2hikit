package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Trail
import org.springframework.web.bind.annotation.*
import java.util.concurrent.atomic.AtomicLong


@RestController
@RequestMapping("/api/v1/trail")
class TrailController {
    private val counter = AtomicLong()

    @GetMapping("/{code}")
    fun getTrail(@PathVariable code: Int): Trail? {
        return null
    }
}