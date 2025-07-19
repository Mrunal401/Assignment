package com.example.assignment.network

import android.os.Handler
import android.os.Looper
import com.example.assignment.model.Country
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

object CountryService {

    var url: String = "https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json"
    //private val url = "https://invalid.url/test.json"

    var postToMainThread: ((() -> Unit) -> Unit) = { block ->
        Handler(Looper.getMainLooper()).post(block)
    }

    var client: OkHttpClient = OkHttpClient()

    fun fetchCountries(
        onSuccess: (List<Country>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        Thread {
            try {
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val json = response.body?.string()
                if (response.isSuccessful && json != null) {
                    val countryList = Gson().fromJson(json, Array<Country>::class.java).toList()
                    postToMainThread { onSuccess(countryList) }
                } else {
                    throw Exception("Failed to fetch countries: ${response.code}")
                }
            } catch (e: Exception) {
                postToMainThread { onError(e) }
            }
        }.start()
    }
}
