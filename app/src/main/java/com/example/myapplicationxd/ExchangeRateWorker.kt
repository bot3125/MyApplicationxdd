package com.example.myapplicationxd

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ExchangeRateWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        val exchangeRate = fetchExchangeRates()
        return if (exchangeRate != null) {
            sendNotification(exchangeRate)
            Result.success()
        } else {
            Result.failure()
        }
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
                Log.d("ExchangeRateWorker", "API response: ${response.body()}")
                val rates = response.body()?.rates
                rates?.let {
                    ExchangeRateData(
                        current = it["USD"] ?: 0.0,
                        min3Months = it["USD"]?.times(0.95) ?: 0.0,
                        min1Year = it["USD"]?.times(0.90) ?: 0.0,
                        min5Years = it["USD"]?.times(0.85) ?: 0.0
                    )
                }
            } else {
                Log.e("ExchangeRateWorker", "API error: ${response.code()}")
                null
            }
        } catch (e: Exception) {
            Log.e("ExchangeRateWorker", "Network error: ${e.message}")
            null
        }
    }


    private fun sendNotification(rate: ExchangeRateData?) {
        val channelId = "ExchangeRateChannel"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Exchange Rate Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Tipo de cambio actualizado")
            .setContentText("El tipo de cambio actual es: $rate")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(applicationContext)) {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                notify(1, builder.build())
            }
        }
    }
}
