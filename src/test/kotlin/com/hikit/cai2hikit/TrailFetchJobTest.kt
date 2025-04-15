package com.hikit.cai2hikit

import com.hikit.cai2hikit.dao.Geometry
import com.hikit.cai2hikit.dao.Properties
import com.hikit.cai2hikit.dao.Trail
import com.hikit.cai2hikit.remote.OsmGeometry
import com.hikit.cai2hikit.remote.OsmProperties
import com.hikit.cai2hikit.remote.OsmTrail
import org.hikit.common.dto.IdToUpdateDate
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


@ExtendWith(MockitoExtension::class)
class TrailFetchJobTest(
    @Mock val mockedTrailClient: TrailRestClient,
    @Mock val trailUpdateHelper: TrailUpdateHelper
) {
    @Test
    fun `should test retrieving one trail member calls`() {
        // given
        val expectedId = "30319"
        val trail = OsmTrail(
            properties = OsmProperties(
                expectedId,
                123,
                "",
                "",
                "",
                "",
                "123",
                "",
                4,
                Date(),
                Date(),
            ),
            geometry = OsmGeometry(
                type = "type",
                coordinates = listOf()
            )
        )
        val idToUpdateDate = IdToUpdateDate(expectedId, LocalDateTime.now())
        `when`(mockedTrailClient.fetchTrailIdsWithinBoundBox())
            .thenReturn(listOf(idToUpdateDate))
        `when`(mockedTrailClient.fetchTrail(expectedId))
            .thenReturn(
                trail
            )

        val systemUnderTest = TrailFetchJob(
            mockedTrailClient,
            trailUpdateHelper
        )

        // when
        systemUnderTest.updateSystem()

        // then
        verify(trailUpdateHelper, times(1)).upsertMoreRecentData(trail, idToUpdateDate)
        verify(mockedTrailClient, times(1)).fetchTrail(expectedId)
    }

    @Test
    fun `should not save null ref trail`() {
        // given
        val expectedId = "30319"
        val osmTrail = OsmTrail(
            properties = OsmProperties(
                expectedId,
                123,
                "",
                "",
                "",
                "",
                null,
                "",
                4,
                Date(),
                Date(),
            ),
            geometry = OsmGeometry(
                type = "type",
                coordinates = listOf()
            )
        )
        `when`(mockedTrailClient.fetchTrail(expectedId))
            .thenReturn(
                osmTrail
            )
        val idToUpdateDate = IdToUpdateDate(expectedId, LocalDateTime.now())
        `when`(mockedTrailClient.fetchTrailIdsWithinBoundBox())
            .thenReturn(listOf(idToUpdateDate))


        val systemUnderTest = TrailFetchJob(
            mockedTrailClient,
            trailUpdateHelper
        )

        // when
        systemUnderTest.updateSystem()

        // then
        verify(mockedTrailClient, times(1)).fetchTrail(expectedId)
        verify(trailUpdateHelper, never()).upsertMoreRecentData(osmTrail, idToUpdateDate)
    }


    @Test
    fun `should test retrieving one trail and updating it with member calls`() {
        // given
        val expectedId = "30319"
        val someMoreRecentDate = LocalDate.of(2024, 2, 20)
        val updatedDate = getDate(someMoreRecentDate)
        val osmTrail = OsmTrail(
            properties = OsmProperties(
                expectedId,
                123, "updatedSource123", "EEA",
                "Monzuno", "Marzabotto", "123", "",
                123, Date(), updatedDate,
            ),
            geometry = OsmGeometry(
                type = "type",
                coordinates = listOf(
                    listOf(
                        listOf(2.2, 3.3)
                    )
                )
            )
        )

        doReturn(osmTrail).`when`(mockedTrailClient).fetchTrail(expectedId)
        val idToUpdateDate = IdToUpdateDate(expectedId, LocalDateTime.now())
        doReturn(listOf(idToUpdateDate)).`when`(mockedTrailClient)
            .fetchTrailIdsWithinBoundBox()

        val systemUnderTest = TrailFetchJob(
            mockedTrailClient,
            trailUpdateHelper
        )

        // when
        systemUnderTest.updateSystem()

        // then
        verify(mockedTrailClient, times(1)).fetchTrail(expectedId)
        verify(trailUpdateHelper, times(1)).upsertMoreRecentData(osmTrail, idToUpdateDate)
    }

    @Test
    fun `should skip saving on fetching error`() {
        // given
        val expectedId = "any"
        doReturn(listOf(IdToUpdateDate(expectedId, LocalDateTime.now()))).`when`(mockedTrailClient)
            .fetchTrailIdsWithinBoundBox()
        doReturn(null).`when`(mockedTrailClient).fetchTrail(expectedId)
        val systemUnderTest = TrailFetchJob(
            mockedTrailClient,
            trailUpdateHelper
        )

        // when
        systemUnderTest.updateSystem()

        // then
        verify(trailUpdateHelper, never()).upsertMoreRecentData(any(OsmTrail::class.java), any(IdToUpdateDate::class.java))
    }


    private fun getDate(someSavedDate: LocalDate): Date =
        Date.from(someSavedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())


    private fun <T> any(type: Class<T>): T = Mockito.any<T>(type)
}