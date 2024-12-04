package com.hikit.cai2hikit

import com.hikit.cai2hikit.testconfig.getTrailRestClient
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

//@ActiveProfiles("test")
@SpringBootTest
class TrailFetchTest(
    @Autowired private val client: TrailRestClient
) {
    private val trailFetchJob = TrailFetchJob(
        client,
        "53.741833",
        "54.810096",
        "20.355102",
        "22.036322"
    )

    @Test
    fun testTrailFetch() {
        println(client.getRestClient())
        trailFetchJob.getTrail()
    }
}