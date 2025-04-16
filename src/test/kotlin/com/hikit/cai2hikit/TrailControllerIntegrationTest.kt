package com.hikit.cai2hikit

import com.hikit.cai2hikit.remote.OsmGeometry
import com.hikit.cai2hikit.remote.OsmProperties
import com.hikit.cai2hikit.remote.OsmTrail
import com.hikit.cai2hikit.processor.Coordinates
import org.hikit.common.dto.IdToUpdateDate
import org.hikit.common.dto.MatchingRequest
import org.hikit.common.dto.StatsTrailMetadata
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.time.LocalDateTime
import java.util.*
import kotlin.test.assertEquals

@SpringBootTest
class TrailControllerIntegrationTest @Autowired constructor(
    private val trailController: TrailController,
    private val trailRepository: TrailRepository,
    private val trailUpdateHelper: TrailUpdateHelper
) {
    private val storedTrail = OsmTrail(
        OsmProperties(
            "1234",
            123,
            "",
            "",
            "",
            "",
            "109",
            "",
            4,
            Date(),
            Date()
        ),
        OsmGeometry(
            "LineString",
            listOf(
                listOf(
                    listOf(1.0, 1.0),
                    listOf(1.1, 1.1)
                )
            )
        )
    )

    @Test
    fun `should store trail and check get by ref endpoint`() {
        trailUpdateHelper.upsertMoreRecentData(
            storedTrail,
            IdToUpdateDate("1234", LocalDateTime.now())
        )
        assertEquals(trailController.getTrailByRef("109")?.first()?.properties?.ref, "109")
    }

    @Test
    fun `should store trail and check get by code endpoint`() {
        trailUpdateHelper.upsertMoreRecentData(
            storedTrail,
            IdToUpdateDate("1234", LocalDateTime.now())
        )
        assertEquals(trailController.getTrail("1234")?.properties?.id, "1234")
    }

    @Test
    fun `should store trail and check match trail endpoint`() {
        val matchingRequest = MatchingRequest(
            "",
            listOf(
                Coordinates(1.0, 1.0),
                Coordinates(1.1, 1.1),
            ),
            StatsTrailMetadata(
                0.0,
                0.0,
                0.0,
                0.0,
                0.0,
                0.0
            )
        )
        trailUpdateHelper.upsertMoreRecentData(
            storedTrail,
            IdToUpdateDate("123", LocalDateTime.now())
        )
        assertEquals(trailController.matchTrail(matchingRequest)?.first()?.properties?.ref, "123")
    }
}