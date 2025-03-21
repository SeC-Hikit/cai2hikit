package com.hikit.cai2hikit

import com.hikit.cai2hikit.dao.GeometryMapper
import org.hikit.common.dto.IdToUpdateDate
import com.hikit.cai2hikit.dao.Trail as daoTrail
import com.hikit.cai2hikit.dao.Geometry as daoGeometry
import org.hikit.common.dto.Trail as dtoTrail
import org.hikit.common.dto.Geometry as dtoGeometry
import org.hikit.common.dto.Properties
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


@ExtendWith(MockitoExtension::class)
class TrailFetchJobTest(
    @Mock val mockedTrailClient: TrailRestClient,
    @Mock val mockedTrailRepository: TrailRepository,
    @Mock val mockedGeometryMapper: GeometryMapper
) {
    @Test
    fun `should test retrieving one trail member calls`() {
        // given
        val expectedId = "30319"
        `when`(mockedTrailClient.fetchTrail(expectedId))
            .thenReturn(
                dtoTrail(
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
                    geometry = dtoGeometry(
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
            mockedGeometryMapper
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
                dtoTrail(
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
                    geometry = dtoGeometry(
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
            mockedGeometryMapper
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
        val savedTrail = dtoTrail(
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
            geometry = dtoGeometry(
                type = "type",
                coordinates = listOf()
            )
        )

        val someMoreRecentDate = LocalDate.of(2024, 2, 20)
        val fetchedTrail = dtoTrail(
            properties = Properties(
                expectedId,
                123, "updatedSource123", "EEA",
                "Monzuno", "Marzabotto", "123", "",
                123, Date(), getDate(someMoreRecentDate),
            ),
            geometry = dtoGeometry(
                type = "type",
                coordinates = listOf(
                    listOf()
                )
            )
        )

        doReturn(fetchedTrail).`when`(mockedTrailClient).fetchTrail(expectedId)
        doReturn(savedTrail).`when`(mockedTrailRepository).findByPropsId(expectedId)
        doReturn(listOf(IdToUpdateDate(expectedId, LocalDateTime.now()))).`when`(mockedTrailClient).fetchTrailIdsWithinBoundBox()

        val systemUnderTest = TrailFetchJob(
            mockedTrailClient,
            mockedTrailRepository,
            mockedGeometryMapper
        )

        // when
        systemUnderTest.updateSystem()

        // then
        verify(mockedTrailClient, times(1)).fetchTrail(expectedId)
        verify(mockedTrailRepository, times(1)).save(argThat {
            trail: dtoTrail -> trail.geometry.coordinates.size == 1 &&
                trail.properties.updatedAt == fetchedTrail.properties.updatedAt
        })
    }

    @Test
    fun `should skip saving on fetching error`() {
        // given
        val expectedId = "any"
        doReturn(listOf(IdToUpdateDate(expectedId, LocalDateTime.now()))).`when`(mockedTrailClient).fetchTrailIdsWithinBoundBox()
        doReturn(null).`when`(mockedTrailClient).fetchTrail(expectedId)
        val systemUnderTest = TrailFetchJob(
            mockedTrailClient,
            mockedTrailRepository,
            mockedGeometryMapper
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