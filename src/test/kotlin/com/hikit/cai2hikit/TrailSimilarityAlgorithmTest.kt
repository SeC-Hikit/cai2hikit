package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Coordinates2D
import com.hikit.cai2hikit.dto.Geometry
import com.hikit.cai2hikit.dto.Properties
import com.hikit.cai2hikit.dto.Trail
import com.hikit.cai2hikit.processor.DTWAlgorithm
import com.hikit.cai2hikit.processor.TrailSimilarityAlgorithm
import org.hikit.common.adapter.AltitudeServiceAdapter
import org.hikit.common.processor.TrailsStatsCalculator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

// Test 1: match 1.0 - perfect match
// Test 2: match 0.0 - horrible match
// Test 3: match ±0.60 - match dati geografici perfetto, match dati calcolati completamente KO
// Test 4: match ±0.40 - match dati geografici KO, perfect match dati calcolati completamente

private val storedTrail0 = Trail(
    properties = Properties(
        "1",
        1,
        "",
        "",
        "",
        "",
        "",
        "",
        4,
        Date(),
        Date(),
    ),
    geometry = Geometry(
        type = "type",
        coordinates = listOf(
            listOf(
                Coordinates2D(
                    43.9653826,
                    11.5473039
                ),
                Coordinates2D(
                    43.9657346,
                    11.5478117
                ),
                Coordinates2D(
                    43.9659758,
                    11.5481934
                ),
                Coordinates2D(
                    43.9661336,
                    11.5484466
                )
            )
        )
    )
)

@ExtendWith(MockitoExtension::class)
class TrailSimilarityAlgorithmTest(
    @Mock val mockedTrailRepository: TrailRepository
) {
    @Test
    fun `should check two identical trails`() {
        `when`(mockedTrailRepository.findByPropsId("0"))
            .thenReturn(storedTrail0)

        val dtwUnderTest = DTWAlgorithm(
            mockedTrailRepository
        )

        val trailSimilarityAlgorithmUnderTest = TrailSimilarityAlgorithm(
            mockedTrailRepository,
            TrailsStatsCalculator(),
            dtwUnderTest
        )
    }

    @Test
    fun `should check two very different trails`() {}

    @Test
    fun `should check data match, geometry mismatch`() {}

    @Test
    fun `should check data mismatch, geometry match`() {}
}