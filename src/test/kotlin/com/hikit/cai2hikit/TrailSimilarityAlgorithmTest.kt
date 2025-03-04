package com.hikit.cai2hikit

import com.hikit.cai2hikit.adapter.AltitudeServiceWrapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

import com.hikit.cai2hikit.dto.Coordinates2D
import com.hikit.cai2hikit.dto.Geometry
import com.hikit.cai2hikit.dto.Properties
import com.hikit.cai2hikit.dto.Trail
import com.hikit.cai2hikit.processor.Coordinates
import com.hikit.cai2hikit.processor.DTWAlgorithm
import com.hikit.cai2hikit.processor.TrailSimilarityAlgorithm
import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyString

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

private val storedTrailWithElevation0: List<Coordinates> = listOf(
    Coordinates(
        43.9653826,
        11.5473039,
        100.0
    ),
    Coordinates(
        43.9657346,
        11.5478117,
        105.0
    ),
    Coordinates(
        43.9659758,
        11.5481934,
        110.0
    ),
    Coordinates(
        43.9661336,
        11.5484466,
        115.0
    )
)

@ExtendWith(MockitoExtension::class)
class TrailSimilarityAlgorithmTest(
    @Mock val mockedTrailRepository: TrailRepository,
    @Mock val mockedAltitudeAdapter: AltitudeServiceWrapper
) {
    @Test
    fun `should check two identical trails`() {
        `when`(mockedTrailRepository.findByPropsId(anyString()))
            .thenReturn(storedTrail0)

        `when`(mockedAltitudeAdapter.mapCoordsWithElevations(anyList()))
            .thenReturn(storedTrailWithElevation0)

        val dtwUnderTest = DTWAlgorithm(
            mockedTrailRepository
        )

        val trailSimilarityAlgorithmUnderTest = TrailSimilarityAlgorithm(
            mockedTrailRepository,
            dtwUnderTest,
            mockedAltitudeAdapter
        )

        val result = trailSimilarityAlgorithmUnderTest.runAlgorithm("0", storedTrail0)
        assertEquals(result, 1.0)
    }

    @Test
    fun `should check two very different trails`() {
        val storedTrail1 = Trail(
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
                            42.9653826,
                            12.5473039
                        ),
                        Coordinates2D(
                            42.9657346,
                            12.5478117
                        ),
                        Coordinates2D(
                            42.9659758,
                            12.5481934
                        ),
                        Coordinates2D(
                            42.9661336,
                            12.5484466
                        )
                    )
                )
            )
        )

        val storedTrail1WithElevation1: List<Coordinates> = listOf(
            Coordinates(
                42.9653826,
                12.5473039,
                300.0
            ),
            Coordinates(
                42.9657346,
                12.5478117,
                305.0
            ),
            Coordinates(
                42.9659758,
                12.5481934,
                310.0
            ),
            Coordinates(
                42.9661336,
                12.5484466,
                315.0
            )
        )

        `when`(mockedTrailRepository.findByPropsId(anyString()))
            .thenReturn(storedTrail0)

        `when`(mockedAltitudeAdapter.mapCoordsWithElevations(anyList()))
            .thenReturn(storedTrailWithElevation0)

        val dtwUnderTest = DTWAlgorithm(
            mockedTrailRepository
        )

        val trailSimilarityAlgorithmUnderTest = TrailSimilarityAlgorithm(
            mockedTrailRepository,
            dtwUnderTest,
            mockedAltitudeAdapter
        )

        val result = trailSimilarityAlgorithmUnderTest.runAlgorithm("0", storedTrail1)
        assertEquals(result, 0.0)
    }

    @Test
    fun `should check data match, geometry mismatch`() {}

    @Test
    fun `should check data mismatch, geometry match`() {}
}