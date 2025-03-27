package com.hikit.cai2hikit

import com.hikit.cai2hikit.dao.DaoTrailMapper
import com.hikit.cai2hikit.dao.Geometry
import com.hikit.cai2hikit.dao.Properties
import com.hikit.cai2hikit.dao.Trail
import com.hikit.cai2hikit.processor.TrailSimilarityProcessor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import kotlin.test.assertEquals


@ExtendWith(MockitoExtension::class)
class TrailControllerTest(
    @Mock val trailRepositoryMock: TrailRepository,
    @Mock val daoTrailMapperMock: DaoTrailMapper,
    @Mock val similarityProcessor: TrailSimilarityProcessor,
) {
    @Test
    fun `should get trail by id`() {
        // GIVEN
        val expectedId = "123"
        val storedTrail = Trail(
            properties = Properties(
                expectedId,
                123,
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
                coordinates = listOf()
            )
        )
        val mappedTrailMock = mock<org.hikit.common.dto.Trail>()
        `when`(trailRepositoryMock.findByPropsId(expectedId))
            .thenReturn(storedTrail)
        `when`(daoTrailMapperMock.mapToDto(storedTrail)).thenReturn(mappedTrailMock)


        // WHEN
        val controllerUnderTest = TrailController(trailRepositoryMock, daoTrailMapperMock, similarityProcessor)
        val returnedTrail = controllerUnderTest.getTrail(expectedId)

        // THEN
        verify(trailRepositoryMock, times(1)).findByPropsId(expectedId)
        assertEquals(returnedTrail, mappedTrailMock)
    }

    @Test
    fun `should get trail by ref`() {
        // GIVEN
        val expectedRef = "101"
        val trail = Trail(
            properties = Properties(
                "123",
                123,
                "",
                "",
                "",
                "",
                expectedRef,
                "",
                4,
                Date(),
                Date()
            ),
            geometry = Geometry(
                type = "type",
                coordinates = listOf()
            )
        )
        val storedTrailList = listOf(
            trail
        )
        val mappedTrailMock = mock<org.hikit.common.dto.Trail>()
        `when`(trailRepositoryMock.findByRef(expectedRef))
            .thenReturn(storedTrailList)
        `when`(daoTrailMapperMock.mapToDto(trail)).thenReturn(mappedTrailMock)


        // WHEN
        val controllerUnderTest = TrailController(trailRepositoryMock, daoTrailMapperMock, similarityProcessor)
        val returnedTrail = controllerUnderTest.getTrailByRef(expectedRef)

        // THEN

        verify(trailRepositoryMock, times(1)).findByRef(expectedRef)
        assertThat(returnedTrail).contains(mappedTrailMock)
        assertThat(returnedTrail).hasSize(1)
    }

    // TODO: add new test to ensure matchTrail
}