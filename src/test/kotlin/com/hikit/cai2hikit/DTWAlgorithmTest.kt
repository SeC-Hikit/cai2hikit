package com.hikit.cai2hikit

import com.hikit.cai2hikit.processor.DTWAlgorithm
import org.hikit.common.dto.Geometry
import org.hikit.common.dto.Properties
import org.hikit.common.dto.Trail
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.anyString
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

// Test prerequisiti: creare un piccolissimo database di sentieri

// ==== Tests dove abbiamo solo un sentiero matchato ==== //

// Test 1: match 0 - input produce output di valore 1
// Test 2: match 1 - input produce output di valore 0
// Test 3: match ±0.5 - input produce output di valore 0.5

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

// TODO - specifica casi di test ulteriori per multisentiero sulla base della vicinanza

@ExtendWith(MockitoExtension::class)
class DTWAlgorithmTest(
    @Mock val mockedTrailRepository: TrailRepository
) {
    @Test
    fun `should check two identical trails`() {

        `when`(mockedTrailRepository.findByPropsId(anyString()))
            .thenReturn(storedTrail0)

        val DTWUnderTest = DTWAlgorithm(
            mockedTrailRepository
        )

        val result = DTWUnderTest.runAlgorithm(
            storedTrail0.geometry.coordinates,
            storedTrail0.geometry.coordinates
        )
        assertEquals(result, 1.0)
    }

    // two Trails ~110km from each other
    @Test
    fun `should check two distant trails`() {
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

        `when`(mockedTrailRepository.findByPropsId(anyString()))
            .thenReturn(storedTrail0)

        val DTWUnderTest = DTWAlgorithm(
            mockedTrailRepository
        )

        val result = DTWUnderTest.runAlgorithm(
            storedTrail0.geometry.coordinates,
            storedTrail1.geometry.coordinates
        )
        assertEquals(result, 0.0)
    }

    // two Trails giving ~ 0.5 distance
    @Test
    fun `should check two close trails`() {
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

        `when`(mockedTrailRepository.findByPropsId(anyString()))
            .thenReturn(storedTrail0)

        val DTWUnderTest = DTWAlgorithm(
            mockedTrailRepository
        )

        val result = DTWUnderTest.runAlgorithm(
            storedTrail0.geometry.coordinates,
            storedTrail1.geometry.coordinates
        )
        assertTrue(result in 0.45..0.55)
    }
}
// TODO: guarda Integration Test su hikit
//@RunWith(SpringRunner.class)
//@SpringBootTest()
//@TestPropertySource(locations = "classpath:application-test.properties")