package com.hikit.cai2hikit

import com.hikit.cai2hikit.dto.Trail
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.springframework.http.MediaType


class TrailFetchJobTest(
    val mockedTrailClient: TrailRestClient
) {
//    var mockedTrailClient: TrailRestClient = mock(TrailRestClient::class.java)

    @Test
    fun `should test retrieving one trail member calls`() {
        //  should I use \${test.fetch.trailid} and application.yml?
        val singleTrailFetch = "http://osm2cai.it/api/v2/hiking-route/30319"
        val trailTestResponse = mockedTrailClient.getRestClient().get()
            .uri(singleTrailFetch)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .body(Trail::class.java)
        print(trailTestResponse)
    }

}