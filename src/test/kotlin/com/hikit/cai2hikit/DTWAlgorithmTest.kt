package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Coordinates2D
import com.hikit.cai2hikit.dto.Geometry
import com.hikit.cai2hikit.dto.Properties
import com.hikit.cai2hikit.dto.Trail
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

// Test prerequisiti: creare un piccolissimo database di sentieri

// ==== Tests dove abbiamo solo un sentiero matchato ==== //

// Test 1: match 0 - input produce output di valore 0
// Test 2: match 1 - input produce output di valore 1
// Test 3: match ±0.5 - input produce output di valore 0.5
// Test 4: match ±0.60 - match dati geografici perfetto, match dati calcolati completamente KO
// Test 5: match ±0.40 - match dati geografici KO, perfect match dati calcolati completamente

// TODO - specifica casi di test ulteriori per multisentiero sulla base della vicinanza

@ExtendWith(MockitoExtension::class)
class DTWAlgorithmTest(
    @Mock val mockedTrailRepository: TrailRepository
) {
    @Test
    fun `should check two identical trails`() {
        val storedTrail0 = Trail(
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
                            11.5473039,
                            43.9653826
                        ),
                        Coordinates2D(
                            11.5478117,
                            43.9657346
                        ),
                        Coordinates2D(
                            11.5481934,
                            43.9659758
                        ),
                        Coordinates2D(
                            11.5484466,
                            43.9661336
                        )
                    )
                )
            )
        )

        `when`(mockedTrailRepository.findByPropsId("0"))
            .thenReturn(storedTrail0)
        `when`(mockedTrailRepository.findByPropsId("1"))
            .thenReturn(storedTrail0)

        val DTWUnderTest = DTWAlgorithm(
            mockedTrailRepository
        )

        val result = DTWUnderTest.runAlgorithm("0", storedTrail0)
        assertEquals(result, 0.0)
    }
}
// TODO: guarda Integration Test su hikit
//@RunWith(SpringRunner.class)
//@SpringBootTest()
//@TestPropertySource(locations = "classpath:application-test.properties")