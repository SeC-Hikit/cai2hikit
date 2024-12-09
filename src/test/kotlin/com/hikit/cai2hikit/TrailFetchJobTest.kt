package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.IdToUpdateDate
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.LocalDateTime


class TrailFetchJobTest(
    @Mock val mockedTrailClient: TrailRestClient
) {

    @Test
    fun `should test retrieving one trail member calls`() {
        // given
        `when`(mockedTrailClient.fetchTrailIdsWithinBoundBox())
            .thenReturn(listOf(IdToUpdateDate("123", LocalDateTime.now())))
        // when
        val systemUnderTest = TrailFetchJob(mockedTrailClient, mock())

        // then
        systemUnderTest.updateSystem()
    }

}