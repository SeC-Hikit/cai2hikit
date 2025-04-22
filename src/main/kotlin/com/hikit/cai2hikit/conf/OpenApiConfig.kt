package com.hikit.cai2hikit.conf

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenAPIConfig {
    @Value("\${openapi.dev-url}")
    private val devUrl: String? = null


    @Bean
    fun myOpenAPI(): OpenAPI {
        val devServer = Server()
        devServer.url(devUrl)

        val info: Info = Info()
            .title("Trail retrieval API")
            .version("1.0")
            .description("This API exposes endpoints to retrieve trails.")

        return OpenAPI().info(info).servers(listOf(devServer))
    }
}