package com.example.assignment.network

import android.os.Handler
import android.os.Looper
import com.example.assignment.model.Country
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request

object CountryService {

    private val url = "https://gist.githubusercontent.com/peymano-wmt/32dcb892b06648910ddd40406e37fdab/raw/db25946fd77c5873b0303b858e861ce724e0dcd0/countries.json"
    //private val url = "https://invalid.url/test.json"

    fun fetchCountries(
        onSuccess: (List<Country>) -> Unit,
        onError: (Exception) -> Unit
    ) {
        Thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder().url(url).build()
                val response = client.newCall(request).execute()
                val json = response.body?.string()
                if (response.isSuccessful && json != null) {
                    //Thread.sleep(3000)
                    val countryList = Gson().fromJson(json, Array<Country>::class.java).toList()
                    Handler(Looper.getMainLooper()).post { onSuccess(countryList) }
                } else {
                    throw Exception("Failed to fetch countries: ${response.code}")
                }
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post { onError(e) }
            }
        }.start()
    }
}
