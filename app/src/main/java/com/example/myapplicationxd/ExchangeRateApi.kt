// ExchangeRateApi.kt
package com.example.myapplicationxd

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

// Interfaz de la API para obtener el tipo de cambio
interface ExchangeRateApi {

    /**
     * Obtiene los tipos de cambio más recientes.
     *
     * @param base La moneda base para la conversión (opcional, por defecto "USD").
     * @param apiKey (opcional) La clave de acceso si la API lo requiere.
     * @return Un objeto Call que envuelve la respuesta del servidor.
     */
    @GET("latest")
    fun getExchangeRate(
        @Query("base") base: String = "USD",  // Moneda base
        @Query("access_key") apiKey: String? = null // Clave API si es requerida
    ): Call<ExchangeRateResponse>
}
