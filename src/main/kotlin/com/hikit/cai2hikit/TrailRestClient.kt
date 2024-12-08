package com.hikit.cai2hikit

import org.springframework.web.client.RestClient

class TrailRestClient(baseUrl: String) {
    private var restClient: RestClient =
        RestClient.builder().baseUrl(baseUrl).build()

    fun getRestClient(): RestClient {
        return restClient
    }
}

