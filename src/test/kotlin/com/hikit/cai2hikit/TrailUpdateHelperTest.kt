package com.hikit.cai2hikit

import com.hikit.cai2hikit.dao.Properties
import com.hikit.cai2hikit.dao.Trail
import com.hikit.cai2hikit.remote.FetchedTrailMapper
import com.hikit.cai2hikit.remote.OsmGeometry
import com.hikit.cai2hikit.remote.OsmProperties
import com.hikit.cai2hikit.remote.OsmTrail
import io.mockk.InternalPlatformDsl.toStr
import org.hikit.common.dto.IdToUpdateDate
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
class TrailUpdateHelperTest(
    @Mock val trailRepository: TrailRepository,
    @Mock val fetchedTrailMapper: FetchedTrailMapper
) {

    @Test
    fun `should update an already existing trail`() {

        // given
        val expectedId = UUID.randomUUID().toStr()
        val input = OsmTrail(
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
        val savedTrail = mock<Trail>()
        val fetchedAndMappedTrail = mock<Trail>()

        val anyOlderDate = LocalDate.of(2024, 2, 20)

        val savedProperties = mock(Properties::class.java)

        `when`(trailRepository.findByPropsId(expectedId)).thenReturn(savedTrail)
        `when`(fetchedTrailMapper.mapToEntity(input)).thenReturn(fetchedAndMappedTrail)
        `when`(savedTrail.properties).thenReturn(savedProperties)
        `when`(savedProperties.updatedAt).thenReturn(getDate(anyOlderDate))


        // when
        val sut = TrailUpdateHelper(trailRepository, fetchedTrailMapper)
        sut.upsertMoreRecentData(input, idToUpdateDate)

        // then
        verify(trailRepository).delete(savedTrail)
        verify(trailRepository).save(fetchedAndMappedTrail)
    }


    @Test
    fun `should create on non existing trail`() {

        // given
        val expectedId = UUID.randomUUID().toStr()
        val input = OsmTrail(
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
        val fetchedAndMappedTrail = mock<Trail>()
        val fetchedProps = mock(Properties::class.java)

        `when`(trailRepository.findByPropsId(expectedId)).thenReturn(null)
        `when`(fetchedTrailMapper.mapToEntity(input)).thenReturn(fetchedAndMappedTrail)
        `when`(fetchedAndMappedTrail.properties).thenReturn(fetchedProps)

        // when
        val sut = TrailUpdateHelper(trailRepository, fetchedTrailMapper)
        sut.upsertMoreRecentData(input, idToUpdateDate)

        // then
        verify(trailRepository).insert(fetchedAndMappedTrail)
    }

    @Test
    fun `should not interact with db when trail is updated`() {

        // given
        val expectedId = UUID.randomUUID().toStr()
        val input = OsmTrail(
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
        val savedTrail = mock<Trail>()
        val fetchedAndMappedTrail = mock<Trail>()


        val savedProperties = mock(Properties::class.java)

        `when`(trailRepository.findByPropsId(expectedId)).thenReturn(savedTrail)
        `when`(fetchedTrailMapper.mapToEntity(input)).thenReturn(fetchedAndMappedTrail)
        `when`(savedTrail.properties).thenReturn(savedProperties)
        `when`(savedProperties.updatedAt).thenReturn(Date())


        // when
        val sut = TrailUpdateHelper(trailRepository, fetchedTrailMapper)
        sut.upsertMoreRecentData(input, idToUpdateDate)

        // then
        verify(trailRepository, never()).delete(savedTrail)
        verify(trailRepository, never()).save(fetchedAndMappedTrail)
        verify(trailRepository, never()).insert(fetchedAndMappedTrail)
    }





    private fun getDate(someSavedDate: LocalDate): Date =
        Date.from(someSavedDate.atStartOfDay(ZoneId.systemDefault()).toInstant())

}
