package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Geometry
import com.hikit.cai2hikit.dto.Properties
import com.hikit.cai2hikit.dto.Trail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*


@ExtendWith(MockitoExtension::class)
class TrailControllerTest(
    @Mock val mockedTrailRepository: TrailRepository
) {
    @Test
    fun `should get trail by id`() {
        val expectedId = "123"
        `when`(mockedTrailRepository.findByPropsId(expectedId))
            .thenReturn(
                Trail(
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
            )

        val controllerUnderTest = TrailController(mockedTrailRepository)

        controllerUnderTest.getTrail(expectedId)

        verify(mockedTrailRepository, times(1)).findByPropsId(expectedId)
    }

    @Test
    fun `should get trail by ref`() {
        val expectedRef = "101"
        `when`(mockedTrailRepository.findByRef(expectedRef))
            .thenReturn(
                listOf(
                    Trail(
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
                )
            )

        val controllerUnderTest = TrailController(mockedTrailRepository)

        controllerUnderTest.getTrailByRef(expectedRef)

        verify(mockedTrailRepository, times(1)).findByRef(expectedRef)
    }
}