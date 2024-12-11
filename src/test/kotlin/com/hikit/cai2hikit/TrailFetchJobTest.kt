package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.*
import com.hikit.cai2hikit.dto.Coordinates2D
import com.hikit.cai2hikit.dto.Properties
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class TrailFetchJobTest(
    @Mock val mockedTrailClient: TrailRestClient
) {
    @Test
    fun `should test retrieving one trail member calls`() {
//        val singleTrail = mockedTrailClient.fetchTrail("30319")
//        println(singleTrail)

        `when`(mockedTrailClient.fetchTrail("30319"))
            .thenReturn(Trail(
                properties = Properties(
                    "123",
                    123,
                    "",
                    "",
                    "",
                    "",
                    "",
                    "",
                    123,
                    Date(),
                    Date(),
                ),
                geometry = Geometry(
                    type = "type",
                    coordinates = listOf<List<Coordinates2D>>()
                )))

        // given
        `when`(mockedTrailClient.fetchTrailIdsWithinBoundBox())
            .thenReturn(listOf(IdToUpdateDate("30319", LocalDateTime.now())))
        // when
        val systemUnderTest = TrailFetchJob(mockedTrailClient, mock())

        // then
        systemUnderTest.updateSystem()

    }
}