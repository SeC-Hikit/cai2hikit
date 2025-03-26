package com.hikit.cai2hikit

import com.hikit.cai2hikit.remote.FetchedTrailMapper
import org.hikit.common.dto.Geometry
import org.hikit.common.dto.IdToUpdateDate
import org.hikit.common.dto.Properties
import org.hikit.common.dto.Trail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import com.hikit.cai2hikit.remote.OsmGeometry as daoGeometry
import com.hikit.cai2hikit.remote.OsmTrail as daoTrail


@ExtendWith(MockitoExtension::class)
class TrailFetchJobTest(
    @Mock val mockedTrailClient: TrailRestClient,
    @Mock val mockedTrailRepository: TrailRepository,
    @Mock val mockedFetchedTrailMapper: FetchedTrailMapper
) {
    @Test
    fun `should test retrieving one trail member calls`() {
        // given
        val expectedId = "30319"
        val trail = Trail(
            properties = Properties(
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
            geometry = Geometry(
                type = "type",
                coordinates = listOf()
            )
        )
        `when`(mockedTrailClient.fetchTrail(expectedId))
            .thenReturn(
                trail
            )

        `when`(mockedFetchedTrailMapper.mapToData(trail.geometry)).thenReturn(mock(daoGeometry::class.java))
        `when`(mockedTrailClient.fetchTrailIdsWithinBoundBox())
            .thenReturn(listOf(IdToUpdateDate(expectedId, LocalDateTime.now())))
        val systemUnderTest = TrailFetchJob(
            mockedTrailClient,
            mockedTrailRepository,
            mockedFetchedTrailMapper
        )

        // when
        systemUnderTest.updateSystem()

        // then
        verify(mockedTrailClient, times(1)).fetchTrail(expectedId)
    }

    @Test
    fun `should not save null ref trail`() {
        // given
        val expectedId = "30319"
        `when`(mockedTrailClient.fetchTrail(expectedId))
            .thenReturn(
                Trail(
                    properties = Properties(
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
                    geometry = Geometry(
                        type = "type",
                        coordinates = listOf()
                    )
                )
            )


        `when`(mockedTrailClient.fetchTrailIdsWithinBoundBox())
            .thenReturn(listOf(IdToUpdateDate(expectedId, LocalDateTime.now())))
        val systemUnderTest = TrailFetchJob(
            mockedTrailClient,
            mockedTrailRepository,
            mockedFetchedTrailMapper
        )

        // when
        systemUnderTest.updateSystem()

        // then
        verify(mockedTrailClient, times(1)).fetchTrail(expectedId)
        verify(mockedTrailRepository, never()).findByPropsId(expectedId)
        verify(mockedTrailRepository, never()).save(any())
    }


    @Test
    fun `should test retrieving one trail and updating it with member calls`() {
        // given
        val expectedId = "30319"
        val someSavedDate = LocalDate.of(2015, 2, 20)
        val savedTrail = daoTrail(
            properties = Properties(
                expectedId,
                123,
                "updatedSource",
                "EE",
                "Monzuno",
                "Vado",
                "123",
                "",
                123,
                Date(),
                getDate(someSavedDate)
            ),
            osmGeometry = daoGeometry(
                type = "type",
                coordinates = listOf()
            )
        )

        val someMoreRecentDate = LocalDate.of(2024, 2, 20)
        val fetchedTrail = Trail(
            properties = Properties(
                expectedId,
                123, "updatedSource123", "EEA",
                "Monzuno", "Marzabotto", "123", "",
                123, Date(), getDate(someMoreRecentDate),
            ),
            geometry = Geometry(
                type = "type",
                coordinates = listOf(
                    listOf(
                        listOf(2.2, 3.3)
                    )
                )
            )
        )
        doReturn(daoGeometry("LineString", listOf(listOf(2.2, 3.3)))).`when`(mockedFetchedTrailMapper)
            .mapToData(fetchedTrail.geometry)
        doReturn(fetchedTrail).`when`(mockedTrailClient).fetchTrail(expectedId)
        doReturn(savedTrail).`when`(mockedTrailRepository).findByPropsId(expectedId)
        doReturn(listOf(IdToUpdateDate(expectedId, LocalDateTime.now()))).`when`(mockedTrailClient)
            .fetchTrailIdsWithinBoundBox()

        val systemUnderTest = TrailFetchJob(
            mockedTrailClient,
            mockedTrailRepository,
            mockedFetchedTrailMapper
        )

        // when
        systemUnderTest.updateSystem()

        // then
        verify(mockedTrailClient, times(1)).fetchTrail(expectedId)
        verify(mockedTrailRepository, times(1)).save(argThat { trail: daoTrail ->
            trail.osmGeometry.coordinates.size == 1 &&
                    trail.properties.updatedAt == fetchedTrail.properties.updatedAt
        })
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
            mockedTrailRepository,
            mockedFetchedTrailMapper
        )

        // when
        systemUnderTest.updateSystem()

        // then
        verify(mockedTrailRepository, never()).findByPropsId(expectedId)
        verify(mockedTrailRepository, never()).save(any())
    }


    private fun getDate(someSavedDate: LocalDate): Date =
        Date.from(someSavedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

}