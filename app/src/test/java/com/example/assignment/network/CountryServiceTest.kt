package com.example.assignment.network

import com.example.assignment.model.Country
import com.google.gson.Gson
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class CountryServiceTest {

    private val mockCountries = listOf(
        Country("USA", "Americas", "US", "Washington D.C."),
        Country("France", "Europe", "FR", "Paris")
    )

    @Before
    fun setup() {
        // Default: success client
        val json = Gson().toJson(mockCountries)
        val responseBody = ResponseBody.create("application/json".toMediaType(), json)
        CountryService.postToMainThread = { block -> block() }

        val mockClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                Response.Builder()
                    .code(200)
                    .message("OK")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(responseBody)
                    .addHeader("content-type", "application/json")
                    .build()
            }
            .build()

        CountryService.client = mockClient
        CountryService.url = "https://mock.test"
    }

    @Test
    fun `fetchCountries returns expected data`() {
        val latch = CountDownLatch(1)
        var result: List<Country>? = null

        CountryService.fetchCountries(
            onSuccess = {
                result = it
                latch.countDown()
            },
            onError = {
                fail("Expected success but got error: ${it.message}")
                latch.countDown()
            }
        )

        latch.await(2, TimeUnit.SECONDS)
        assertNotNull(result)
        assertEquals(2, result?.size)
        assertEquals("USA", result?.get(0)?.name)
        assertEquals("Paris", result?.get(1)?.capital)
    }

    @Test
    fun `fetchCountries calls onError on non-200 response`() {
        val responseBody = ResponseBody.create("application/json".toMediaType(), "")
        val errorClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                Response.Builder()
                    .code(404)
                    .message("Not Found")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(responseBody)
                    .build()
            }
            .build()
        CountryService.client = errorClient
        CountryService.url = "https://mock.test"
        val latch = CountDownLatch(1)
        var error: Exception? = null

        CountryService.fetchCountries(
            onSuccess = { fail("Should not succeed") },
            onError = { error = it; latch.countDown() }
        )
        latch.await(2, TimeUnit.SECONDS)
        assertNotNull(error)
    }

    @Test
    fun `fetchCountries calls onError on null response body`() {
        val nullBodyClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                Response.Builder()
                    .code(200)
                    .message("OK")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(null)
                    .build()
            }
            .build()
        CountryService.client = nullBodyClient
        CountryService.url = "https://mock.test"
        val latch = CountDownLatch(1)
        var error: Exception? = null

        CountryService.fetchCountries(
            onSuccess = { fail("Should not succeed") },
            onError = { error = it; latch.countDown() }
        )
        latch.await(2, TimeUnit.SECONDS)
        assertNotNull(error)
    }

    @Test
    fun `fetchCountries calls onError on invalid JSON`() {
        val invalidJson = "{ invalid json }"
        val responseBody = ResponseBody.create("application/json".toMediaType(), invalidJson)
        val invalidJsonClient = OkHttpClient.Builder()
            .addInterceptor { chain ->
                Response.Builder()
                    .code(200)
                    .message("OK")
                    .request(chain.request())
                    .protocol(Protocol.HTTP_1_1)
                    .body(responseBody)
                    .build()
            }
            .build()
        CountryService.client = invalidJsonClient
        CountryService.url = "https://mock.test"
        val latch = CountDownLatch(1)
        var error: Exception? = null

        CountryService.fetchCountries(
            onSuccess = { fail("Should not succeed") },
            onError = { error = it; latch.countDown() }
        )
        latch.await(2, TimeUnit.SECONDS)
        assertNotNull(error)
    }
}