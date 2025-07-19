package com.example.assignment

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.adapter.CountryAdapter
import com.example.assignment.network.CountryService
import com.example.assignment.model.Country
import com.google.gson.Gson

import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var countryAdapter: CountryAdapter
    private var countryList: List<Country> = emptyList()

    private lateinit var progressBar: ProgressBar
    private lateinit var retryButton: Button
    private lateinit var errorMessage: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = findViewById(R.id.progressBar)
        retryButton = findViewById(R.id.retryButton)
        errorMessage = findViewById(R.id.errorMessage)

        retryButton.setOnClickListener {
            fetchData()
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val json = savedInstanceState?.getString("data_json")

        if (!json.isNullOrEmpty()) {
            countryList = Gson().fromJson(json, Array<Country>::class.java).toList()
            showCountries(countryList)
            showContent()
        } else {
            fetchData()
        }

    }

    private fun fetchData() {
        //showLoading()
        progressBar.visibility = View.VISIBLE
        retryButton.visibility = View.GONE
        errorMessage.visibility = View.GONE
        recyclerView.visibility = View.GONE
        CountryService.fetchCountries(
            onSuccess = { countries ->
                countryList = countries
                showCountries(countries)
                //showContent()
                progressBar.visibility = View.GONE
                retryButton.visibility = View.GONE
                errorMessage.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            },
            onError = { error ->
                progressBar.visibility = View.GONE
                retryButton.visibility = View.VISIBLE
                errorMessage.visibility = View.VISIBLE
                errorMessage.text = "Failed to load data. Please try again."
                recyclerView.visibility = View.GONE
            }
        )
    }

    private fun showCountries(countries: List<Country>) {
        val cleaned = countries.filterNotNull() // Safety filter
        if (cleaned.any { it.name == null || it.region == null || it.code == null || it.capital == null }) {
            Toast.makeText(this, "Invalid country data found", Toast.LENGTH_SHORT).show()
        }
        countryAdapter = CountryAdapter(cleaned)
        recyclerView.adapter = countryAdapter
    }

    private fun showContent() {
        progressBar.visibility = View.GONE
        retryButton.visibility = View.GONE
        errorMessage.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        if (countryList.isNotEmpty()) {
            outState.putString("data_json", Gson().toJson(countryList))
        }
    }
}