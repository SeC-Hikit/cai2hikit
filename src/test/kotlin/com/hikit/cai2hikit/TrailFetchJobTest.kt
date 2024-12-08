package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Trail
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.springframework.http.MediaType


class TrailFetchJobTest(
    @Mock val mockedTrailClient: RestClient
) {

    @Test
    fun `should test retrieving one trail member calls`() {
        // given
//        `when`(mockedTrailClient.getClient()).
        // when

        // then
        //  should I use \${test.fetch.trailid} and application.yml?
        val singleTrailFetch = "http://osm2cai.it/api/v2/hiking-route/30319"
        val trailTestResponse = mockedTrailClient.getClient().get()
            .uri(singleTrailFetch)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Trail::class.java)
        print(trailTestResponse)
    }

}