package com.hikit.cai2hikit

import Trail
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicLong

@RestController
class TrailController {
    private val counter = AtomicLong()

    @GetMapping("/trail/{code}")
    fun getTrail(@PathVariable code: Int): Trail {
        return Trail(counter.incrementAndGet(), code)
    }
}