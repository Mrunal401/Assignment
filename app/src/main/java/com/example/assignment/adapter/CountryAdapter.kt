package com.example.assignment.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment.R
import com.example.assignment.model.Country

class CountryAdapter(private val countries: List<Country>) :
    RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {

        inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val nameRegionCode: TextView = itemView.findViewById(R.id.nameRegionCode)
            val capital: TextView = itemView.findViewById(R.id.capital)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_country, parent, false)
            return CountryViewHolder(view)
        }

        override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
            val country = countries[position]
            holder.nameRegionCode.text = "${country.name ?: "Unknown"}, ${country.region ?: "Unknown"}    ${country.code ?: "--"}"
            holder.capital.text = country.capital ?: "No Capital"
        }

        override fun getItemCount(): Int = countries.size
}