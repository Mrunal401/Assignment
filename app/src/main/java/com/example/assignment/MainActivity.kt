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

//        if (savedInstanceState != null) {
//            val json = savedInstanceState.getString("data_json")
//            if (!json.isNullOrEmpty()) {
//                countryList = Gson().fromJson(json, Array<Country>::class.java).toList()
//                showCountries(countryList)
//                return
//            }
//        }
//
//        // Only fetch if no saved data
//        if (countryList.isEmpty()) {
//            fetchData()
//        }

        // Restore state if available
//        savedInstanceState?.getString("data_json")?.let {
//            countryList = Gson().fromJson(it, Array<Country>::class.java).toList()
//            showCountries(countryList)
//        } ?: run {
//            fetchData()
//        }
    }

//    private fun fetchData() {
//        CountryService.fetchCountries(
//            onSuccess = { countries ->
//                countryList = countries
//                showCountries(countries)
//            },
//            onError = { error ->
//                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
//            }
//        )
//    }


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
//                showError(error.message ?: "Unknown error")
//                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
    }


//    private fun fetchData() {
//        progressBar.visibility = View.VISIBLE
//        retryButton.visibility = View.GONE
//        errorMessage.visibility = View.GONE
//        recyclerView.visibility = View.GONE
//
//        CountryService.fetchCountries(
//            onSuccess = { countries ->
//                progressBar.visibility = View.GONE
//                recyclerView.visibility = View.VISIBLE
//                retryButton.visibility = View.GONE
//                errorMessage.visibility = View.GONE
//
//                countryList = countries
//                showCountries(countries)
//            },
//            onError = { error ->
//                progressBar.visibility = View.GONE
//                retryButton.visibility = View.VISIBLE
//                errorMessage.visibility = View.VISIBLE
//                recyclerView.visibility = View.GONE
//
//                //errorMessage.text = "Failed to load data. Please try again."
//                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
//            }
//        )
//    }


    private fun showCountries(countries: List<Country>) {
        val cleaned = countries.filterNotNull() // Safety filter
        if (cleaned.any { it.name == null || it.region == null || it.code == null || it.capital == null }) {
            Toast.makeText(this, "Invalid country data found", Toast.LENGTH_SHORT).show()
        }
        countryAdapter = CountryAdapter(cleaned)
        recyclerView.adapter = countryAdapter

//        countryAdapter = CountryAdapter(countries)
//        recyclerView.adapter = countryAdapter
    }

    private fun showLoading() {
        progressBar.visibility = View.VISIBLE
        retryButton.visibility = View.GONE
        errorMessage.visibility = View.GONE
        recyclerView.visibility = View.GONE
    }

    private fun showError(message: String) {
        progressBar.visibility = View.GONE
        retryButton.visibility = View.VISIBLE
        errorMessage.visibility = View.VISIBLE
        errorMessage.text = "Failed to load data. Please try again."
        recyclerView.visibility = View.GONE
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
        //outState.putString("data_json", Gson().toJson(countryList))
    }
}

//
//class MainActivity : AppCompatActivity() {
//    private lateinit var recyclerView: RecyclerView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        recyclerView = findViewById(R.id.countriesRecyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        CountryService.fetchCountries(
//            onSuccess = { countries ->
//                val adapter = CountryAdapter(countries)
//                recyclerView.adapter = adapter
//            },
//            onError = { error ->
//                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
//            }
//        )
//    }
//}








//
//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import com.example.assignment.ui.theme.AssignmentTheme

//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            AssignmentTheme {
//                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    AssignmentTheme {
//        Greeting("Android")
//    }
//}
//---------------------------------
//
//class MainActivity : AppCompatActivity() {
//
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var countryAdapter: CountryAdapter
//    private var countryList: List<Country> = emptyList()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        recyclerView = findViewById(R.id.recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        // Restore state if available
//        savedInstanceState?.getString("data_json")?.let {
//            countryList = Gson().fromJson(it, Array<Country>::class.java).toList()
//            showCountries(countryList)
//        } ?: run {
//            fetchData()
//        }
//    }
//
//    private fun fetchData() {
//        CountryService.fetchCountries(
//            onSuccess = { countries ->
//                countryList = countries
//                showCountries(countries)
//            },
//            onError = { error ->
//                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_LONG).show()
//            }
//        )
//    }
//
//    private fun showCountries(countries: List<Country>) {
//        countryAdapter = CountryAdapter(countries)
//        recyclerView.adapter = countryAdapter
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        outState.putString("data_json", Gson().toJson(countryList))
//    }
//}
