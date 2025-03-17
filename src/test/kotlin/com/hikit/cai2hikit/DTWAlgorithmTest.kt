package com.hikit.cai2hikit

import com.hikit.cai2hikit.processor.Coordinates
import com.hikit.cai2hikit.processor.DTWAlgorithm
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

// Test prerequisiti: creare un piccolissimo database di sentieri

// ==== Tests dove abbiamo solo un sentiero matchato ==== //

// Test 1: match 0 - input produce output di valore 1
// Test 2: match 1 - input produce output di valore 0
// Test 3: match ±0.5 - input produce output di valore 0.5

private val storedTrail0 = listOf(
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
)


// TODO - specifica casi di test ulteriori per multisentiero sulla base della vicinanza

@ExtendWith(MockitoExtension::class)
class DTWAlgorithmTest {
    @Test
    fun `should check two identical trails`() {
        val dtwUnderTest = DTWAlgorithm()

        val result = dtwUnderTest.run(
            storedTrail0,
            storedTrail0
        )
        assertEquals(result, 1.0)
    }

    // two Trails ~110km from each other
    @Test
    fun `should check two distant trails`() {
        val storedTrail1 = listOf(
            Coordinates(
                42.9653826,
                12.5473039
            ),
            Coordinates(
                42.9657346,
                12.5478117
            ),
            Coordinates(
                42.9659758,
                12.5481934
            ),
            Coordinates(
                42.9661336,
                12.5484466
            )
        )
        val dtwUnderTest = DTWAlgorithm()

        val result = dtwUnderTest.run(
            storedTrail0,
            storedTrail1
        )
        assertEquals(result, 0.0)
    }

    // two Trails giving ~ 0.5 distance
    @Test
    fun `should check two close trails`() {
        val storedTrail1 = listOf(
            Coordinates(
                43.9653826,
                11.5473039
            ),
            Coordinates(
                43.7657346,
                11.4178117
            ),
            Coordinates(
                43.7659758,
                11.4081934
            ),
            Coordinates(
                43.9661336,
                11.5484466
            )
        )

        val dtwUnderTest = DTWAlgorithm()

        val result = dtwUnderTest.run(
            storedTrail0,
            storedTrail1
        )
        print(result)
        assertTrue(result in 0.45..0.55)
    }
}
// TODO: guarda Integration Test su hikit
//@RunWith(SpringRunner.class)
//@SpringBootTest()
//@TestPropertySource(locations = "classpath:application-test.properties")