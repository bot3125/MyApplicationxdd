package com.example.myapplicationxd

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import android.util.Log

class MainActivity : AppCompatActivity() {

    private lateinit var exchangeRateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        exchangeRateTextView = findViewById(R.id.exchangeRateTextView)
        exchangeRateTextView.text = getString(R.string.fetching_exchange_rate)

        val workRequest = PeriodicWorkRequestBuilder<ExchangeRateWorker>(1, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ExchangeRateWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )

        obtenerTipoDeCambio()
    }

    private fun obtenerTipoDeCambio() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangerate-api.com/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val api = retrofit.create(ExchangeRateApi::class.java)

// Llamada con parámetros opcionales
        api.getExchangeRate(base = "EUR", apiKey = "tu_clave_aqui").enqueue(object : Callback<ExchangeRateResponse> {
            override fun onResponse(
                call: Call<ExchangeRateResponse>,
                response: Response<ExchangeRateResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val rate = response.body()?.rates?.get("USD") ?: 0.0
                    exchangeRateTextView.text = getString(R.string.exchange_rate_text, rate)
                } else {
                    exchangeRateTextView.text = getString(R.string.error_fetching_rate)
                }
            }

            override fun onFailure(call: Call<ExchangeRateResponse>, t: Throwable) {
                Log.e("MainActivity", "Error al obtener el tipo de cambio: ${t.message}")
            }
        
        })
    }

    private fun actualizarUI(data: ExchangeRateData) {
        exchangeRateTextView.text = getString(R.string.exchange_rate_text, data.current)
        Log.d("MainActivity", "Tipo de cambio actualizado: ${data.current}")
    }

    private fun fetchExchangeRates(): ExchangeRateData? {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.exchangerate-api.com/v4/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ExchangeRateApi::class.java)

        return try {
            val response = api.getExchangeRate().execute()
            if (response.isSuccessful) {
                response.body()?.let { body ->
                    Log.d("fetchExchangeRates", "Datos obtenidos correctamente de la API")
                    ExchangeRateData(
                        current = body.rates["USD"] ?: 0.0,
                        min3Months = body.rates["USD"]?.times(0.95) ?: 0.0,
                        min1Year = body.rates["USD"]?.times(0.90) ?: 0.0,
                        min5Years = body.rates["USD"]?.times(0.85) ?: 0.0
                    )
                }
            } else {
                Log.e("fetchExchangeRates", "Error en la respuesta de la API: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("fetchExchangeRates", "Error de red: ${e.message}")
            null
        }
    }

    private fun showAlertDialog(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("OK", null)
        val dialog = builder.create()
        dialog.show()
    }
}
