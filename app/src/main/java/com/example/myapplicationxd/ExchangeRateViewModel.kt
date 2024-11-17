package com.example.myapplicationxd

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExchangeRateViewModel : ViewModel() {
    private val api: ExchangeRateApi = Retrofit.Builder()
        .baseUrl("https://api.exchangerate-api.com/v4/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ExchangeRateApi::class.java)

    fun getExchangeRate() = liveData(Dispatchers.IO) {
        try {
            val response = api.getExchangeRate().execute()
            if (response.isSuccessful) {
                val rate = response.body()?.rates?.get("USD") ?: 0.0
                emit(ExchangeRateData(rate, rate * 0.95, rate * 0.90, rate * 0.85))
            } else {
                emit(null) // Manejar la respuesta no exitosa
            }
        } catch (e: Exception) {
            emit(null) // Manejar errores de red
        }
    }
}
