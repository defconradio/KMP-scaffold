package com.example.shared

import com.example.shared.di.initKoin
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.context.stopKoin
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class KoinModuleTest {
    @AfterTest
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun koinResolvesCoreDependencies() {
        val koinApp = initKoin {}
        val json: Json = koinApp.koin.get()
        val httpClient: HttpClient = koinApp.koin.get()

        assertNotNull(json)
        assertNotNull(httpClient)
        httpClient.close()
    }
}


