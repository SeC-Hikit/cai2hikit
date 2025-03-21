package com.hikit.cai2hikit

import com.hikit.cai2hikit.dao.Trail as daoTrail
import com.hikit.cai2hikit.dao.Geometry as daoGeometry
import org.hikit.common.dto.Properties
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*
import kotlin.test.assertEquals


@ExtendWith(MockitoExtension::class)
class TrailControllerTest(
    @Mock val mockedTrailRepository: TrailRepository
) {
    @Test
    fun `should get trail by id`() {
        val expectedId = "123"
        val storedTrail = daoTrail(
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
            geometry = daoGeometry(
                type = "type",
                coordinates = listOf()
            )
        )
        `when`(mockedTrailRepository.findByPropsId(expectedId))
            .thenReturn(storedTrail)

        val controllerUnderTest = TrailController(mockedTrailRepository)

        val returnedTrail = controllerUnderTest.getTrail(expectedId)

        verify(mockedTrailRepository, times(1)).findByPropsId(expectedId)
        assertEquals(returnedTrail, storedTrail)
    }

    @Test
    fun `should get trail by ref`() {
        val expectedRef = "101"
        val storedTrailList = listOf(
            daoTrail(
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
                geometry = daoGeometry(
                    type = "type",
                    coordinates = listOf()
                )
            )
        )
        `when`(mockedTrailRepository.findByRef(expectedRef))
            .thenReturn(storedTrailList)

        val controllerUnderTest = TrailController(mockedTrailRepository)

        val returnedTrail = controllerUnderTest.getTrailByRef(expectedRef)

        verify(mockedTrailRepository, times(1)).findByRef(expectedRef)
        assertEquals(returnedTrail, storedTrailList)
    }
}