package com.hikit.cai2hikit

import com.hikit.cai2hikit.adapter.AltitudeServiceWrapper
import com.hikit.cai2hikit.dao.GeometryMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

import com.hikit.cai2hikit.dao.Trail as daoTrail
import com.hikit.cai2hikit.dao.Geometry as daoGeometry
import com.hikit.cai2hikit.processor.Coordinates
import org.hikit.common.dto.Trail as dtoTrail
import org.hikit.common.dto.Geometry as dtoGeometry
import org.hikit.common.processor.Coordinates as comCoordinates
import com.hikit.cai2hikit.processor.DTWAlgorithm
import com.hikit.cai2hikit.processor.TrailSimilarityAlgorithm
import org.hikit.common.dto.*
import org.hikit.common.dto.Properties
import org.hikit.common.processor.TrailsStatsCalculator
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.mockito.ArgumentMatchers.anyList

// Test 1: match 1.0 - perfect match
// Test 2: match 0.0 - horrible match
// Test 3: match ±0.60 - match dati geografici perfetto, match dati calcolati completamente KO
// Test 4: match ±0.40 - match dati geografici KO, perfect match dati calcolati completamente

private val storedTrail0 = daoTrail(
    Properties(
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
    daoGeometry(
        "",
        listOf(
                listOf(
                    43.9653826,
                    11.5473039
                ),
                listOf(
                    43.9657346,
                    11.5478117
                    ),
                listOf(
                    43.9659758,
                    11.5481934
                    ),
                listOf(
                    43.9661336,
                    11.5484466
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

val requestData = MatchingRequest(
    "0",
    listOf(
        Coordinates(
            43.9653826,
            11.5473039
        ),
        Coordinates(
            43.9657346,
            11.5478117
        ),
        Coordinates(
            43.9659758,
            11.5481934
        ),
        Coordinates(
            43.9661336,
            11.5484466
        )
    ),
    StatsTrailMetadata(
        15.0,
        0.0,
        152.14106,
        0.0,
        115.0,
        100.0
    )
)

@ExtendWith(MockitoExtension::class)
class TrailSimilarityAlgorithmTest(
    @Mock val mockedTrailRepository: TrailRepository,
    @Mock val mockedAltitudeAdapter: AltitudeServiceWrapper,
    @Mock val mockedGeoTrailRepository: GeoTrailRepository
) {
    @Test
    fun `should check two identical trails`() {
        `when`(mockedTrailRepository.findAll())
            .thenReturn(listOf(storedTrail0))

        `when`(mockedAltitudeAdapter.mapCoordsWithElevations(anyList()))
            .thenReturn(storedTrailWithElevation0)

        val dtwUnderTest = DTWAlgorithm()

        val trailSimilarityAlgorithmUnderTest = TrailSimilarityAlgorithm(
            dtwUnderTest,
            mockedAltitudeAdapter,
            TrailsStatsCalculator(),
            mockedGeoTrailRepository,
            GeometryMapper()
        )

        val result = trailSimilarityAlgorithmUnderTest.run(requestData)
        assertEquals(result[0].accuracy, 1.0)
    }

    @Test
    fun `should check two very different trails`() {
        val storedTrail1 = dtoTrail(
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
            geometry = dtoGeometry(
                type = "type",
                coordinates = listOf(
                    listOf(
                        42.9653826,
                        12.5473039
                    ),
                    listOf(
                        42.9657346,
                        12.5478117
                    ),
                    listOf(
                        42.9659758,
                        12.5481934
                    ),
                    listOf(
                        42.9661336,
                        12.5484466
                    )
                )
            )
        )

        val storedTrailWithElevation1: List<Coordinates> = listOf(
            Coordinates(
                42.9653826,
                12.5473039,
                300.0
            ),
            Coordinates(
                42.9657346,
                12.5478117,
                290.0
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

//        `when`(mockedTrailRepository.findAll())
//            .thenReturn(listOf(storedTrail1))

        `when`(mockedAltitudeAdapter.mapCoordsWithElevations(anyList()))
            .thenReturn(storedTrailWithElevation1)

        val dtwUnderTest = DTWAlgorithm()

        val trailSimilarityAlgorithmUnderTest = TrailSimilarityAlgorithm(
            dtwUnderTest,
            mockedAltitudeAdapter,
            TrailsStatsCalculator(),
            mockedGeoTrailRepository,
            GeometryMapper()
        )

        val result = trailSimilarityAlgorithmUnderTest.run(requestData)
        assertTrue(result[0].accuracy < 0.1)
    }

    @Test
    fun `should check data match, geometry mismatch`() {
        val storedTrail1 = dtoTrail(
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
            geometry = dtoGeometry(
                type = "type",
                coordinates = listOf(
                    listOf(
                        42.9653826,
                        12.5473039
                    ),
                    listOf(
                        42.9657346,
                        12.5478117
                    ),
                    listOf(
                        42.9659758,
                        12.5481934
                    ),
                    listOf(
                        42.9661336,
                        12.5484466
                    )
                )
            )
        )

        val storedTrailWithElevation1: List<Coordinates> = listOf(
            Coordinates(
                42.9653826,
                12.5473039,
                100.0
            ),
            Coordinates(
                42.9657346,
                12.5478117,
                105.0
            ),
            Coordinates(
                42.9659758,
                12.5481934,
                110.0
            ),
            Coordinates(
                42.9661336,
                12.5484466,
                115.0
            )
        )

        `when`(mockedAltitudeAdapter.mapCoordsWithElevations(anyList()))
            .thenReturn(storedTrailWithElevation1)

        val dtwUnderTest = DTWAlgorithm()

        val trailSimilarityAlgorithmUnderTest = TrailSimilarityAlgorithm(
            dtwUnderTest,
            mockedAltitudeAdapter,
            TrailsStatsCalculator(),
            mockedGeoTrailRepository,
            GeometryMapper()
        )

        val result = trailSimilarityAlgorithmUnderTest.run(requestData)
        assertTrue(0.35 < result[0].accuracy && result[0].accuracy < 0.45)
    }

    @Test
    fun `should check data mismatch, geometry match`() {
        val storedTrail1 = dtoTrail(
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
            geometry = dtoGeometry(
                type = "type",
                coordinates = listOf(
                    listOf(
                        43.9653826,
                        11.5473039
                    ),
                    listOf(
                        43.9657346,
                        11.5478117
                    ),
                    listOf(
                        43.9659758,
                        11.5481934
                    ),
                    listOf(
                        43.9661336,
                        11.5484466
                    )
                )
            )
        )


        val storedTrailWithElevation1: List<Coordinates> = listOf(
            Coordinates(
                43.9653826,
                11.5473039,
                10.0
            ),
            Coordinates(
                43.9657346,
                11.5478117,
                1050.0
            ),
            Coordinates(
                43.9659758,
                11.5481934,
                110.0
            ),
            Coordinates(
                43.9661336,
                11.5484466,
                1150.0
            )
        )

        `when`(mockedAltitudeAdapter.mapCoordsWithElevations(anyList()))
            .thenReturn(storedTrailWithElevation1)

        val dtwUnderTest = DTWAlgorithm()

        val trailSimilarityAlgorithmUnderTest = TrailSimilarityAlgorithm(
            dtwUnderTest,
            mockedAltitudeAdapter,
            TrailsStatsCalculator(),
            mockedGeoTrailRepository,
            GeometryMapper()
        )

        val result = trailSimilarityAlgorithmUnderTest.run(requestData)
        assertTrue(0.55 < result[0].accuracy && result[0].accuracy < 0.65)
    }
}